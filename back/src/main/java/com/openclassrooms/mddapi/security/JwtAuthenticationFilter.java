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
 * 
 * @author Cécile UMECKER
 
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Processes each HTTP request to validate JWT tokens from cookies.
     * 
     * This method is invoked once per request to perform JWT authentication. It:
     * 1. Checks if authentication already exists in the security context (to avoid redundant processing)
     * 2. Searches for the "access_token" cookie in the request
     * 3. Extracts and validates the JWT token using JwtService
     * 4. Creates an authentication token and sets it in the SecurityContextHolder if valid
     * 5. Passes the request to the next filter in the chain
     * 
     * If no access token cookie is found or the token is invalid, the request proceeds
     * without authentication, allowing public endpoints to be accessed.
     * 
     * @param request the HTTP request to process
     * @param response the HTTP response
     * @param filterChain the filter chain to continue request processing
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs during request processing
     */
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
