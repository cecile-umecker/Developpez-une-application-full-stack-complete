package com.openclassrooms.mddapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.mddapi.Utils.CookieUtil;
import com.openclassrooms.mddapi.dto.LoginDTO;
import com.openclassrooms.mddapi.dto.MessageDTO;
import com.openclassrooms.mddapi.dto.RegisterDTO;
import com.openclassrooms.mddapi.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {
  
  private final AuthService authService;

    @Operation(summary = "Login user", description = "Authenticate a user and set JWT cookies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<MessageDTO> login(@RequestBody LoginDTO request, HttpServletResponse response) {
        try {
            authService.login(request, response);
            return ResponseEntity.ok(new MessageDTO("Login successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new MessageDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Register user", description = "Register a new account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Registration successful"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
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

    @Operation(summary = "Refresh token", description = "Generate a new access token using the refresh token cookie")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid or missing refresh token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        authService.refreshToken(request, response);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Logout user", description = "Delete authentication cookies to log out the user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout successful"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @PostMapping("/logout")
    public ResponseEntity<MessageDTO> logout(HttpServletResponse response) {
        CookieUtil.clearCookies(response);
        return ResponseEntity.ok(new MessageDTO("Logout successful"));
    }

}