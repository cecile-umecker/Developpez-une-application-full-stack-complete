package com.openclassrooms.mddapi.services;

import java.util.Arrays;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.Utils.CookieUtil;
import com.openclassrooms.mddapi.dto.LoginDTO;
import com.openclassrooms.mddapi.dto.RegisterDTO;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;

/**
 * Service responsible for authentication and authorization operations in the MDD API.
 * 
 * This service handles user authentication workflows including registration, login,
 * and token refresh mechanisms. It manages JWT token generation and cookie-based
 * token storage for maintaining user sessions.
 * 
 * Key responsibilities:
 * - User registration with validation and password encryption
 * - User login with credential verification
 * - JWT access and refresh token generation
 * - Token refresh for extending user sessions
 * - Secure cookie management for token storage
 * 
 * The service uses BCrypt password encoding for security and implements a dual-token
 * system (access token and refresh token) to balance security and user experience.
 * All authentication operations validate user credentials and token validity before
 * proceeding.
 */

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void login(LoginDTO request, HttpServletResponse response) {
        User user = userRepository.findByUsernameOrEmail(request.getLogin(), request.getLogin())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtService.generateToken(user.getId().toString(), false);
        String refreshToken = jwtService.generateToken(user.getId().toString(), true);

        CookieUtil.addCookies(response, accessToken, refreshToken);
    }

    public void register(RegisterDTO request) {
        if (userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail()).isPresent()) {
            throw new RuntimeException("Username or email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) throw new RuntimeException("No cookies found");

        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refresh_token".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));


        String userId = jwtService.extractUserId(refreshToken);

        boolean exists = userRepository.findById(Long.parseLong(userId)).isPresent();

        if (!exists) {
            throw new RuntimeException("User not found");
        }

        if (!jwtService.isTokenValid(refreshToken, userId)) {
            throw new RuntimeException("Invalid refresh token");
        }

        var user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = jwtService.generateToken(user.getId().toString(), false);
        String newRefreshToken = jwtService.generateToken(user.getId().toString(), true);

        CookieUtil.addCookies(response, newAccessToken, newRefreshToken);
    }
}

