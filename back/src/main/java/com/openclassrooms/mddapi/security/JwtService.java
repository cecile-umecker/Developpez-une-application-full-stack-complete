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
 * 
 * @author Cécile UMECKER
 
 */

@Service
public class JwtService {

    private final String jwtSecret;
    private final long accessExpiration;
    private final long refreshExpiration;

    /**
     * Constructs a new JwtService and initializes token expiration settings.
     * 
     * This constructor loads JWT configuration from system properties:
     * - JWT_SECRET: The secret key used for token signing and verification
     * - JWT_EXPIRATION: The default expiration time (in milliseconds) for access tokens
     * 
     * The refresh token expiration is set to 7 days (604800000 milliseconds).
     */
    public JwtService() {
        this.jwtSecret = System.getProperty("JWT_SECRET");
        long defaultExpiration = Long.parseLong(System.getProperty("JWT_EXPIRATION"));
        this.accessExpiration = defaultExpiration;
        this.refreshExpiration = 7 * 24 * 60 * 60 * 1000;
    }

    /**
     * Generates a JWT token for the specified user.
     * 
     * This method creates a JWT token containing the user ID as the subject claim,
     * along with issued-at and expiration timestamps. The token is signed using
     * HMAC SHA-256 algorithm with the configured secret key.
     * 
     * The expiration time depends on the token type:
     * - Access token: Uses the configured access expiration (typically 1 hour)
     * - Refresh token: Uses 7 days expiration
     * 
     * @param userId the user ID to embed in the token
     * @param isRefreshToken true to generate a refresh token, false for an access token
     * @return the generated JWT token as a compact string
     */
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

    /**
     * Extracts the user ID from a JWT token.
     * 
     * This method parses and validates the JWT token signature, then extracts
     * the subject claim which contains the user ID. The token must be signed
     * with the correct secret key to be successfully parsed.
     * 
     * @param token the JWT token to parse
     * @return the user ID extracted from the token's subject claim
     * @throws io.jsonwebtoken.JwtException if the token is invalid or expired
     */
    public String extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates a JWT token for a specific user.
     * 
     * This method performs comprehensive token validation by:
     * 1. Extracting the user ID from the token
     * 2. Verifying the extracted user ID matches the provided user ID
     * 3. Checking that the token has not expired
     * 
     * Any exception during validation (invalid signature, malformed token, etc.)
     * results in the token being considered invalid.
     * 
     * @param token the JWT token to validate
     * @param userId the expected user ID to match against the token
     * @return true if the token is valid and belongs to the specified user, false otherwise
     */
    public boolean isTokenValid(String token, String userId) {
        try {
            String subject = extractUserId(token);
            return (subject.equals(userId) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if a JWT token has expired.
     * 
     * This private helper method parses the token and compares its expiration
     * timestamp against the current date/time. The token is considered expired
     * if its expiration time is before the current moment.
     * 
     * @param token the JWT token to check
     * @return true if the token has expired, false if still valid
     */
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
