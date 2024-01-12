package com.example.dgbackend.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration cors = new CorsConfiguration();
                    cors.setAllowedOrigins(List.of("*", "http://localhost:3000", "http://localhost:8080")); // 추후 수정
                    cors.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));
                    // cookie 비활성화
                    cors.setAllowCredentials(false);
                    // Authorization Header 노출
                    cors.addExposedHeader("Authorization");
                    return cors;
                }))
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(
                                        "/swagger-ui/**"
                                        ,"/oauth/**"
                                        ,"/favicon.ico"
                                        , "/**"
                                ).permitAll() // 추후에 hasRole() 설정
                                .anyRequest().permitAll());
//        http
//                .oauth2Login()
//                .authorizationEndpoint().baseUri("/oauth/authorize")
//                .and()
//                .redirectionEndpoint().baseUri("/oauth/callback")
//                .and()
//                .userInfoEndpoint()
//                .userService()
//                .and()
//                .successHandler();
//        http.addFilterBefore()

        return http.build();
    }
}
