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
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  
  private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<MessageDTO> login(@RequestBody LoginDTO request, HttpServletResponse response) {
        try {
            authService.login(request, response);
            return ResponseEntity.ok(new MessageDTO("Login successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new MessageDTO(e.getMessage()));
        }
    }

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

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        authService.refreshToken(request, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageDTO> logout(HttpServletResponse response) {
        CookieUtil.clearCookies(response);
        return ResponseEntity.ok(new MessageDTO("Logout successful"));
    }

}