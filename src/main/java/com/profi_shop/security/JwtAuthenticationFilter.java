package com.profi_shop.security;


import com.profi_shop.exceptions.UnauthenticatedException;
import com.profi_shop.model.User;
import com.profi_shop.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private JwtTokenProvider jwtTokenProvider;
    private CustomUserDetailsService userDetailsService;

    private JwtAuthenticationFilter(){}

    @Autowired
    private JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService){
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String jwt = extractTokenFromRequest(request);
            System.out.println("jwt = " +jwt);
            if(StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)){
                Long userId = jwtTokenProvider.getUserIdFromToken(jwt);

                User userDetails = userDetailsService.loadUserById(userId);
                System.out.println("found user " + userDetails.getLastname());
                System.out.println(userDetails.getAuthorities());
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        filterChain.doFilter(request,response);
    }

    public static String extractTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        System.out.println("Im extracting cookies");
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    System.out.println(cookie.getName());
                    // Предполагаем, что токен хранится в куке с именем "jwtToken"
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
