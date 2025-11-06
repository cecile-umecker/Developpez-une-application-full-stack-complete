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
        // Vérifie si username ou email déjà pris
        if (userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail()).isPresent()) {
            throw new RuntimeException("Username or email already exists");
        }

        // Création utilisateur avec password hashé
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        // Pas de token généré ici, juste création utilisateur
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) throw new RuntimeException("No cookies found");

        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refresh_token".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));


        // Extraction de l'ID utilisateur depuis le token
        String userId = jwtService.extractUserId(refreshToken);

        // Vérification que l'ID correspond bien à un utilisateur existant
        boolean exists = userRepository.findById(Long.parseLong(userId)).isPresent();

        if (!exists) {
            throw new RuntimeException("User not found");
        }

        // Vérification de la validité du token
        if (!jwtService.isTokenValid(refreshToken, userId)) {
            throw new RuntimeException("Invalid refresh token");
        }

        // Récupération de l'utilisateur
        var user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Génération des nouveaux tokens
        String newAccessToken = jwtService.generateToken(user.getId().toString(), false);
        String newRefreshToken = jwtService.generateToken(user.getId().toString(), true);

        // Ajout des cookies à la réponse
        CookieUtil.addCookies(response, newAccessToken, newRefreshToken);
    }
}

