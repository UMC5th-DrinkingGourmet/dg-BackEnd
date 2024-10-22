package com.example.dgbackend.global.jwt.filter;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.jwt.JwtProvider;
import com.example.dgbackend.global.jwt.service.AuthService;
import com.example.dgbackend.global.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;
    private final AuthService authService;

    // JWT를 이용하여 사용자 인증 및 권한 부여를 처리
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (isPublicUri(requestURI)) {
            filterChain.doFilter(request, response);
            return ;
        }

        String authorizationHeader = request.getHeader("Authorization");

        log.info("=====================token x"+ authorizationHeader);

        if (authorizationHeader == null) {
            request.setAttribute("exception", ErrorStatus._EMPTY_JWT);
        }

        String accessToken = jwtProvider.getJwtTokenFromHeader(request);
        if (isBearer(authorizationHeader) && doNotLogout(accessToken)) {
            String jwtToken = authorizationHeader.substring(7);

            // token 유효성 검증
            if (jwtProvider.isValidToken(jwtToken) == 0) {
                request.setAttribute("exception", ErrorStatus._EXPIRED_TOKEN);
            } else if(jwtProvider.isValidToken(jwtToken) == -1) {
                request.setAttribute("exception", ErrorStatus._INVALID_JWT);
            }

            // token을 활용하여 Member 정보 검증
            jwtProvider.getAuthenticationFromToken(jwtToken);

            // Refresh Token 레디스 검사
            Member loginMember = jwtProvider.getMemberFromToken(jwtToken);
            String id = loginMember.getProvider() + "_" + loginMember.getProviderId();
            log.info("-------------------로그인 멤버" + redisUtil.getData(id));

            // Redis에 해당 RefreshToken이 있는지 검사
            if (!jwtProvider.validateRefreshToken(id, request)) {
                log.info("------------login false");
                request.setAttribute("exception", ErrorStatus._REFRESH_TOKEN_NOT_FOUND);
            }
        }

        filterChain.doFilter(request, response);
    }

    // 검증이 필요없는 URI 작성 (추후 변경)
    private boolean isPublicUri(String requestURI) {
        return
                requestURI.startsWith("/swagger-ui/") ||
//                        requestURI.startsWith("/") ||
                        requestURI.startsWith("/v3/api-docs") ||
                        requestURI.startsWith("/favicon.ico") ||
                        requestURI.startsWith("/auth/");
    }


    // "Bearer "로 시작하는지 확인
    private boolean isBearer(String authorizationHeader) {
        return authorizationHeader.startsWith("Bearer ");
    }

    private boolean doNotLogout(String accessToken) {
        String isLogout = redisUtil.getData(accessToken);
        return isLogout.equals("false");
    }
}
