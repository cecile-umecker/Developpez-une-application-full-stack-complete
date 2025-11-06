package com.openclassrooms.mddapi.Utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Utility class for managing HTTP cookies in the MDD API application.
 * 
 * This class provides static helper methods for creating, configuring, and clearing
 * authentication cookies used to store JWT tokens. It handles both access tokens
 * and refresh tokens with appropriate security settings and expiration times.
 * 
 * Cookie configuration:
 * - HttpOnly flag set to true for security (prevents JavaScript access)
 * - Path set to "/" for application-wide availability
 * - Access token expires after 1 hour (3600 seconds)
 * - Refresh token expires after 7 days (604800 seconds)
 * 
 * This utility supports the stateless authentication mechanism by storing JWT tokens
 * in secure HTTP-only cookies, protecting against XSS attacks while maintaining
 * user sessions across requests.
 */

public class CookieUtil {

    public static void addCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        Cookie accessCookie = new Cookie("access_token", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(60 * 60);

        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }

    public static void clearCookies(HttpServletResponse response) {
        Cookie accessCookie = new Cookie("access_token", null);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0);

        Cookie refreshCookie = new Cookie("refresh_token", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }
}
