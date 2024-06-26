package com.example.dgbackend.global.config;

import com.example.dgbackend.global.jwt.filter.CustomAuthenticationEntryPoint;
import com.example.dgbackend.global.jwt.filter.JwtAuthenticationFilter;
import com.example.dgbackend.global.jwt.handler.CustomLogoutHandler;
import java.util.List;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomLogoutHandler customLogoutHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(HttpBasicConfigurer::disable)
            .csrf(CsrfConfigurer::disable)
            .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                CorsConfiguration cors = new CorsConfiguration();
                cors.setAllowedOrigins(
                    List.of("https://drink-gourmet.kro.kr", "http://localhost:8080"));
                cors.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));
                // cookie 활성화
                cors.setAllowCredentials(true);
                // Authorization Header 노출
                cors.addExposedHeader("Authorization");
                return cors;
            }))
            .sessionManagement(
                configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize ->
                authorize
                    .requestMatchers(
                        "/v3/api-docs/**"
                        , "/swagger-ui/**"
                        //, "/**"
                        , "/favicon.ico"
                        , "/auth/**"
                        , "/logout"
                    ).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

        //.anyRequest().permitAll());

        http
            .logout((logout) ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(customLogoutHandler));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
