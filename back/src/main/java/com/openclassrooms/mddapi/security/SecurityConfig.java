package com.openclassrooms.mddapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * Security configuration class for the MDD API application.
 * 
 * This configuration class sets up Spring Security with JWT-based authentication
 * and authorization. It defines security filters, password encoding, authentication
 * management, and access control rules for the application endpoints.
 * 
 * Key security features configured:
 * - Stateless session management using JWT tokens
 * - BCrypt password encoding for secure password storage
 * - Custom JWT authentication filter for token validation
 * - Public endpoints for authentication operations (register, login, refresh)
 * - Protected endpoints requiring valid JWT authentication
 * - CSRF protection disabled (appropriate for stateless JWT architecture)
 * 
 * The security filter chain processes requests before Spring's default
 * UsernamePasswordAuthenticationFilter to validate JWT tokens from cookies.
 * 
 * @author Cécile UMECKER
 
 */

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    /**
     * Provides a BCrypt password encoder bean for secure password hashing.
     * 
     * This encoder is used throughout the application to hash passwords before
     * storage and to verify passwords during authentication. BCrypt is a strong
     * adaptive hash function that includes a salt and is resistant to brute-force
     * attacks.
     * 
     * @return PasswordEncoder instance using BCrypt algorithm
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures and provides the authentication manager bean.
     * 
     * This method sets up the authentication manager with the custom UserDetailsService
     * for loading user information and the BCrypt password encoder for password verification.
     * The authentication manager is used during login to validate user credentials.
     * 
     * @param http the HttpSecurity configuration object
     * @return configured AuthenticationManager instance
     * @throws Exception if configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsService)
                   .passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    /**
     * Configures the security filter chain for HTTP requests.
     * 
     * This method defines the complete security configuration including:
     * - CSRF protection disabled (suitable for stateless JWT authentication)
     * - Stateless session management (no server-side sessions)
     * - Public access to authentication endpoints (register, login, refresh)
     * - All other endpoints require authentication
     * - Custom JWT authentication filter added before standard authentication
     * 
     * The filter chain intercepts all requests and applies security rules before
     * they reach the controllers.
     * 
     * @param http the HttpSecurity configuration object
     * @return configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Creates and provides the JWT authentication filter bean.
     * 
     * This method instantiates the custom JWT authentication filter with the
     * required dependencies (JwtService and UserDetailsService). The filter
     * is responsible for extracting and validating JWT tokens from cookies
     * on each request.
     * 
     * @return configured JwtAuthenticationFilter instance
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService, userDetailsService);
    }
}
