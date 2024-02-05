package com.example.dgbackend.global.jwt.resolver;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.global.jwt.JwtProvider;
import com.example.dgbackend.global.jwt.annotation.MemberObject;
import com.example.dgbackend.global.jwt.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


/*
Header에 있는 토큰을 이용하여 유저 정보를 가져오는 클래스
 */
@Component
@RequiredArgsConstructor
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtProvider jwtProvider;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isUserIdAnnotation = parameter.getParameterAnnotation(MemberObject.class) != null;
        boolean isMemeberClass = Member.class.equals(parameter.getParameterType());

        return isUserIdAnnotation && isMemeberClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader("Authorization");
        if(token == null)
            return null;

        return jwtProvider.getMemberFromToken(token.split(" ")[1]);
    }
}
