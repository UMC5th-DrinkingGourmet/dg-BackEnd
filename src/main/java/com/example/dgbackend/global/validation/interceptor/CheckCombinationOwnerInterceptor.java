package com.example.dgbackend.global.validation.interceptor;

import com.example.dgbackend.domain.combination.service.CombinationQueryService;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import com.example.dgbackend.global.jwt.JwtProvider;
import com.example.dgbackend.global.validation.annotation.CheckCombinationOwner;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckCombinationOwnerInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final CombinationQueryService combinationQueryService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Handler가 메소드인 경우에만 진행
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // 핸들러 메소드의 매개변수를 가져와서 어노테이션이 있는지 확인
            MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
            for (MethodParameter parameter : methodParameters) {
                if (parameter.hasParameterAnnotation(CheckCombinationOwner.class)) {
                    // JWT를 통해 로그인 사용자 정보 조회
                    Member loginMember = getLoginMember(request);

                    // URL에서 combinationId 추출
                    Long combinationId = extractCombinationId(request);

                    log.info("===================================CheckCombinationOwnerInterceptor combinationId = " + combinationId);
                    // 로그인한 사용자가 작성자인지 확인
                    if (loginMember != null) {
                        Boolean isOwner = combinationQueryService.isCombinationOwner(combinationId, loginMember);
                        if (!isOwner) {
                            throw new ApiException(ErrorStatus._FORBIDDEN_MEMBER_REQUEST);
                        }
                    } else {
                        throw new ApiException(ErrorStatus._UNAUTHORIZED);
                    }
                }
            }
        }
        return true;
    }


    private Member getLoginMember(HttpServletRequest request) {
        String token = jwtProvider.getJwtTokenFromHeader(request);

        return jwtProvider.getMemberFromToken(token.split(" ")[1]);
    }

    private Long extractCombinationId(HttpServletRequest request) {

        String requestURI = request.getRequestURI();
        String[] uriParts = requestURI.split("/");
        for (int i = 0; i < uriParts.length - 1; i++) {
            if (uriParts[i].matches("\\d+")) {
                return Long.parseLong(uriParts[i]);
            }
        }

        throw new ApiException(ErrorStatus._UNAUTHORIZED);
    }
}
