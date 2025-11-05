package com.openclassrooms.mddapi.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

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

    /**
     * Génère un JWT pour l'utilisateur.
     *
     * @param userId ID ou username de l'utilisateur
     * @param isRefreshToken true si c'est un refresh token
     * @return le JWT compacté
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
     * Extrait l'ID ou username de l'utilisateur depuis le token.
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
     * Vérifie si le token est valide (bien signé et non expiré).
     */
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
