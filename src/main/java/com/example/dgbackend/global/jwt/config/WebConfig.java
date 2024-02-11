package com.example.dgbackend.global.jwt.config;

import com.example.dgbackend.global.jwt.resolver.MemberArgumentResolver;

import com.example.dgbackend.global.validation.interceptor.CheckCombinationOwnerInterceptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final MemberArgumentResolver memberArgumentResolver;
    private final CheckCombinationOwnerInterceptor checkCombinationOwnerInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkCombinationOwnerInterceptor);
    }
}