package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.*;
import com.openclassrooms.mddapi.models.*;
import com.openclassrooms.mddapi.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserResponseDTO getCurrentUser() {
      User user = getAuthenticatedUser();
      return mapToUserResponseDTO(user);
  }

  public UserResponseDTO updateCurrentUser(UserUpdateRequestDTO dto) {
    User user = getAuthenticatedUser();

    if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
        user.setUsername(dto.getUsername());
    }

    if (dto.getEmail() != null && !dto.getEmail().isBlank() &&
        !dto.getEmail().equals(user.getEmail())) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "The email is already used."
            );
        }
        user.setEmail(dto.getEmail());
    }

    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
    }

    User updatedUser = userRepository.save(user);
    return mapToUserResponseDTO(updatedUser);
  }


  User getAuthenticatedUser() {
    // L'identifiant est stocké dans le JWT
    String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
    Long userId = Long.valueOf(userIdStr);

    return userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "User not found"
            ));
  }

  private UserResponseDTO mapToUserResponseDTO(User user) {
      UserResponseDTO dto = new UserResponseDTO();
      dto.setUsername(user.getUsername());
      dto.setEmail(user.getEmail());
      return dto;
  }
}
