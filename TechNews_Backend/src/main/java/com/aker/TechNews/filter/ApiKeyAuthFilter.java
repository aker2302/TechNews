package com.aker.TechNews.filter;

import com.aker.TechNews.Service.implementation.ClientAuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Autowired
    private ClientAuthenticationService clientAuthenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        clientAuthenticationService.validateApiKey(request).ifPresent(SecurityContextHolder.getContext()::setAuthentication);

        filterChain.doFilter(request, response);
    }
}
