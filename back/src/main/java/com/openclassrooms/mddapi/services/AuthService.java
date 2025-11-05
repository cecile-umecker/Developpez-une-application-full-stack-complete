package com.openclassrooms.mddapi.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.dto.LoginDTO;
import com.openclassrooms.mddapi.dto.LoginResponseDTO;
import com.openclassrooms.mddapi.dto.RegisterDTO;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.JwtService;

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
}

