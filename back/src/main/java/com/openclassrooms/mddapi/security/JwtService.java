package com.openclassrooms.mddapi.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Service for managing JWT (JSON Web Token) operations in the MDD API application.
 * 
 * This service handles the creation, validation, and parsing of JWT tokens used for
 * authentication and authorization. It supports both access tokens (short-lived) and
 * refresh tokens (long-lived) for maintaining user sessions.
 * 
 * Key responsibilities:
 * - Generate JWT tokens with configurable expiration times
 * - Extract user information from tokens
 * - Validate token authenticity and expiration
 * - Manage separate expiration times for access and refresh tokens
 * 
 * The service uses HMAC SHA-256 algorithm for token signing and retrieves
 * configuration from system properties (JWT_SECRET and JWT_EXPIRATION).
 * 
 * Access tokens typically expire after 1 hour, while refresh tokens last 7 days.
 */

@Service
public class JwtService {

    private final String jwtSecret;
    private final long accessExpiration;
    private final long refreshExpiration;

    public JwtService() {
        this.jwtSecret = System.getProperty("JWT_SECRET");
        long defaultExpiration = Long.parseLong(System.getProperty("JWT_EXPIRATION")); // ex: 3600000ms
        this.accessExpiration = defaultExpiration; // 1h
        this.refreshExpiration = 7 * 24 * 60 * 60 * 1000; // 7 jours pour refresh token
    }

    public String generateToken(String userId, boolean isRefreshToken) {
        long now = System.currentTimeMillis();
        long expiry = now + (isRefreshToken ? refreshExpiration : accessExpiration);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expiry))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token, String userId) {
        try {
            String subject = extractUserId(token);
            return (subject.equals(userId) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}
