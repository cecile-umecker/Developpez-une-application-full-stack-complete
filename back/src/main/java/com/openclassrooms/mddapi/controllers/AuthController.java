package com.openclassrooms.mddapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.mddapi.dto.LoginDTO;
import com.openclassrooms.mddapi.dto.MessageDTO;
import com.openclassrooms.mddapi.dto.RegisterDTO;
import com.openclassrooms.mddapi.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

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
}