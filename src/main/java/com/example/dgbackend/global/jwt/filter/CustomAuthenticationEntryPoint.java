package com.example.dgbackend.global.jwt.filter;

import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorStatus exception = (ErrorStatus) request.getAttribute("exception");

        log.info("=---------------final", request.getAttribute("exception"));

        if (exception.equals(ErrorStatus._EMPTY_JWT)) {
            exceptionHandler(response, ErrorStatus._EMPTY_JWT);
        } else if (exception.equals(ErrorStatus._REFRESH_TOKEN_NOT_FOUND)) {
            log.info("-------------------------refresh", request);
            exceptionHandler(response, ErrorStatus._REFRESH_TOKEN_NOT_FOUND);
        } else if (exception.equals(ErrorStatus._EXPIRED_TOKEN)) {
            exceptionHandler(response, ErrorStatus._EXPIRED_TOKEN);
        } else if (exception.equals(ErrorStatus._INVALID_JWT)) {
            exceptionHandler(response, ErrorStatus._INVALID_JWT);
        }

        return;
    }

    public void exceptionHandler(HttpServletResponse response, ErrorStatus error) throws IOException {
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//
//        final Map<String, Object> body = new HashMap<>();
//        body.put("isSuccess", "fail");
//        body.put("code", error.getCode());
//        body.put("message", error.getMessage());
//
//        final ObjectMapper mapper = new ObjectMapper();
//        mapper.writeValue(response.getOutputStream(), body);

        //response.setStatus(error.getCode());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("message", error.getMessage());
        responseJson.put("code", error.getCode());

        response.getWriter().print(responseJson);
    }
}
