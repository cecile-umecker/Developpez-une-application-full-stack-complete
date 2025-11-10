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
 * 
 * @author Cécile UMECKER
 
 */

public class CookieUtil {

    /**
     * Adds authentication cookies to the HTTP response.
     * 
     * This method creates and configures both access and refresh token cookies
     * with appropriate security settings and expiration times. Both cookies are
     * HttpOnly for XSS protection and have application-wide path scope.
     * 
     * Cookie details:
     * - Access token: expires in 1 hour (3600 seconds)
     * - Refresh token: expires in 7 days (604800 seconds)
     * 
     * @param response the HTTP response to add cookies to
     * @param accessToken the JWT access token to store in a cookie
     * @param refreshToken the JWT refresh token to store in a cookie
     */
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

    /**
     * Clears authentication cookies from the HTTP response.
     * 
     * This method removes both access and refresh token cookies by setting their
     * values to null and expiration to 0. This effectively logs out the user by
     * deleting the authentication tokens from the client. The cookies maintain
     * the same HttpOnly and path settings to ensure proper deletion.
     * 
     * @param response the HTTP response to clear cookies from
     */
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
