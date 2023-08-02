package com.profi_shop.security;


import com.google.gson.Gson;
import com.profi_shop.auth.response.InvalidLoginResponse;
import com.profi_shop.exceptions.UnauthenticatedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String redirectPage;
        if (authException instanceof BadCredentialsException) {
            // Обработка ошибки неверных учетных данных (логин/пароль)
            redirectPage = "/error1/error?message=badCredentials"; // Направляем на страницу с URL "/error1"
        }
        else {
            // Обработка других ошибок аутентификации
            redirectPage = "/error1/error?message=unAuthenticate"; // Направляем на общую страницу ошибки
        }
        response.sendRedirect(redirectPage);
    }
}
