package com.openclassrooms.mddapi.security;

import java.io.IOException;
import java.util.Arrays;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT authentication filter for processing and validating JWT tokens from HTTP cookies.
 * 
 * This filter intercepts incoming HTTP requests to extract and validate JWT tokens
 * stored in cookies. It extends OncePerRequestFilter to ensure the filter is executed
 * only once per request.
 * 
 * The filter performs the following operations:
 * - Checks if authentication is already present in the security context
 * - Extracts the JWT token from the "access_token" cookie
 * - Validates the token using the JwtService
 * - Sets the authentication in the SecurityContextHolder if valid
 * 
 * This approach provides stateless authentication by validating tokens on each request
 * without maintaining server-side session state.
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                Arrays.stream(cookies)
                    .filter(c -> "access_token".equals(c.getName()))
                    .findFirst()
                    .ifPresent(cookie -> {
                        String token = cookie.getValue();
                        String userId = jwtService.extractUserId(token);
                        if (jwtService.isTokenValid(token, userId)) {
                            UsernamePasswordAuthenticationToken auth = 
                                new UsernamePasswordAuthenticationToken(userId, null, null);
                            SecurityContextHolder.getContext().setAuthentication(auth);
                        }
                    });
            }
        }

        filterChain.doFilter(request, response);
    }
}
