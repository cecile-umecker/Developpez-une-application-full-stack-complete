package com.openclassrooms.mddapi.services;

import java.util.Arrays;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.Utils.CookieUtil;
import com.openclassrooms.mddapi.dto.LoginDTO;
import com.openclassrooms.mddapi.dto.LoginResponseDTO;
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

    public LoginResponseDTO login(LoginDTO request) {
        User user = userRepository.findByUsernameOrEmail(request.getLogin(), request.getLogin())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtService.generateToken(user.getId().toString(), false);
        String refreshToken = jwtService.generateToken(user.getId().toString(), true);

        return new LoginResponseDTO(accessToken, refreshToken, "Login successful");
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

        // Récupération du refreshToken depuis les cookies
        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        // Extraction de l'identifiant de l'utilisateur depuis le token
        String userId = jwtService.extractUserId(refreshToken);

        // Vérification de la validité du token
        if (!jwtService.isTokenValid(refreshToken, userId)) {
            throw new RuntimeException("Invalid refresh token");
        }

        // Récupération de l'utilisateur en base
        var user = userRepository.findByEmail(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Génération des nouveaux tokens
        String newAccessToken = jwtService.generateToken(user.getEmail(), false); // 1h
        String newRefreshToken = jwtService.generateToken(user.getEmail(), true); // 7 jours

        // Ajout des cookies à la réponse
        CookieUtil.addCookies(response, newAccessToken, newRefreshToken);
    }

}

