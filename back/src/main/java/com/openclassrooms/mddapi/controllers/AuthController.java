package com.openclassrooms.mddapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.mddapi.Utils.CookieUtil;
import com.openclassrooms.mddapi.dto.LoginDTO;
import com.openclassrooms.mddapi.dto.MessageDTO;
import com.openclassrooms.mddapi.dto.RegisterDTO;
import com.openclassrooms.mddapi.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Controller handling authentication-related endpoints.
 * Provides functionality for user login, registration, token refresh and logout operations.
 * All endpoints are mapped under the "/auth" path.
 * 
 * This controller uses JWT (JSON Web Token) based authentication with cookie storage
 * and implements RESTful endpoints for user authentication flows.
 *
 * The following operations are supported:
 * - User login with credentials
 * - New user registration 
 * - Access token refresh using refresh token
 * - User logout with cookie cleanup
 * 
 * @author Cécile UMECKER
 
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  
  private final AuthService authService;

    /**
     * Authenticates a user with their credentials.
     * 
     * This endpoint validates the user's login credentials (username/email and password)
     * and generates JWT access and refresh tokens stored in HTTP-only cookies.
     * 
     * @param request the login credentials containing username/email and password
     * @param response the HTTP response where authentication cookies will be set
     * @return ResponseEntity with success message (200 OK) or error message (401 Unauthorized)
     */
    @PostMapping("/login")
    public ResponseEntity<MessageDTO> login(@RequestBody LoginDTO request, HttpServletResponse response) {
        try {
            authService.login(request, response);
            return ResponseEntity.ok(new MessageDTO("Login successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new MessageDTO(e.getMessage()));
        }
    }

    /**
     * Registers a new user in the system.
     * 
     * This endpoint creates a new user account with the provided registration details.
     * It validates that the username and email are unique before creating the account.
     * The password is encrypted before storage.
     * 
     * @param request the registration details including username, email, and password
     * @return ResponseEntity with success message (201 Created) or error message (409 Conflict)
     */
    @PostMapping("/register")
    public ResponseEntity<MessageDTO> register(@RequestBody RegisterDTO request) {
        try {
            authService.register(request);
            return ResponseEntity.status(201)
                    .body(new MessageDTO("Registration successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(409)
                    .body(new MessageDTO(e.getMessage()));
        }
    }

    /**
     * Refreshes the user's access token using a valid refresh token.
     * 
     * This endpoint extracts the refresh token from the request cookies, validates it,
     * and generates new access and refresh tokens if valid. The new tokens are stored
     * in HTTP-only cookies, extending the user's session.
     * 
     * @param request the HTTP request containing the refresh token cookie
     * @param response the HTTP response where new authentication cookies will be set
     * @return ResponseEntity with 200 OK status if successful
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        authService.refreshToken(request, response);
        return ResponseEntity.ok().build();
    }

    /**
     * Logs out the current user by clearing authentication cookies.
     * 
     * This endpoint clears both access and refresh token cookies from the client,
     * effectively terminating the user's session. No server-side session invalidation
     * is needed due to the stateless JWT authentication approach.
     * 
     * @param response the HTTP response where cookies will be cleared
     * @return ResponseEntity with success message (200 OK)
     */
    @PostMapping("/logout")
    public ResponseEntity<MessageDTO> logout(HttpServletResponse response) {
        CookieUtil.clearCookies(response);
        return ResponseEntity.ok(new MessageDTO("Logout successful"));
    }

}