package com.example.dgbackend.global.security;

import com.example.dgbackend.global.jwt.JwtAuthenticationFilter;
import com.example.dgbackend.global.security.oauth2.handler.CustomLogoutHandler;
import com.example.dgbackend.global.security.oauth2.handler.OAuth2AuthSuccessHandler;
import com.example.dgbackend.global.security.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthSuccessHandler oAuth2AuthSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomLogoutHandler customLogoutHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration cors = new CorsConfiguration();
                    cors.setAllowedOrigins(List.of("*", "https://drink-gourmet.kro.kr", "http://localhost:8080")); // 추후 수정
                    cors.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));
                    // cookie 활성화
                    cors.setAllowCredentials(true);
                    // Authorization Header 노출
                    cors.addExposedHeader("Authorization");
                    return cors;
                }))
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(
                                        "/swagger-ui/**"
                                        , "/login/**"
                                        , "/oauth/**"
                                        , "/favicon.ico"
                                        , "/**"
                                        , "/auth/**"
                                        , "/logout"
                                ).permitAll() // 추후에 hasRole() 설정
                                .anyRequest().permitAll());
        http
                .oauth2Login((oauth2Login) ->
                        oauth2Login
                                .authorizationEndpoint(authorizationEndpoint ->
                                        authorizationEndpoint
                                                .baseUri("/login")
                                )
                                .redirectionEndpoint(redirectEndPoint ->
                                        redirectEndPoint
                                                .baseUri("/login/oauth2/code/*")
                                )
                                .userInfoEndpoint(userInfoEndPoint ->
                                        userInfoEndPoint
                                                .userService(customOAuth2UserService)
                                )
                                .successHandler(oAuth2AuthSuccessHandler)
                )
                .logout((logout) ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessHandler(customLogoutHandler));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}