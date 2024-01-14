package com.example.dgbackend.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    // JWT를 이용하여 사용자 인증 및 권한 부여를 처리
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (isPublicUri(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && isBearer(authorizationHeader)) {
            String jwtToken = authorizationHeader.substring(7);

            // token 유효성 검증
            jwtProvider.isValidToken(jwtToken);

            // token을 활용하여 Member 정보 검증
            jwtProvider.getAuthenticationFromToken(jwtToken);
        }

        filterChain.doFilter(request, response);
    }

    // 검증이 필요없는 URI 작성 (추후 변경)
    private boolean isPublicUri(String requestURI) {
        return
                requestURI.startsWith("/swagger-ui/**") ||
                requestURI.startsWith("/**") ||
                requestURI.startsWith("/favicon.ico") ||
                requestURI.startsWith("/login/**");
    }

    // "Bearer "로 시작하는지 확인
    private boolean isBearer(String authorizationHeader) {
        return authorizationHeader.startsWith("Bearer ");
    }
}
