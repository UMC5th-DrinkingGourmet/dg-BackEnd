package com.example.dgbackend.global.jwt.handler;

import com.example.dgbackend.global.jwt.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLogoutHandler implements LogoutSuccessHandler {

    private final AuthService authService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        authService.logout(request, response);

        // 로그아웃 후 리다이렉션
        String redirectUrl = "/login";
        response.sendRedirect(redirectUrl);
    }

}