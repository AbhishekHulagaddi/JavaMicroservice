package com.rim.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InternalSecretFilter extends OncePerRequestFilter {

    private static final String INTERNAL_SECRET = "AbhishekMH";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        
        
        // Allow public endpoints
        if (path.contains("/authenticate")
                || path.contains("/swagger")
                || path.contains("/v3/api-docs")
                || path.contains("/actuator")) {

            filterChain.doFilter(request, response);
            return;
        }

        String secret = request.getHeader("Secret");

        if (secret == null || !secret.equals(INTERNAL_SECRET)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access denied: Invalid internal secret");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
