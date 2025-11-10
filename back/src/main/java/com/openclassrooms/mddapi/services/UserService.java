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

/**
 * Service responsible for managing user profile operations in the MDD API.
 * 
 * This service provides business logic for user profile management, including
 * retrieving and updating user information. It handles authentication context
 * to identify the current user and manages profile updates with validation.
 * 
 * Key responsibilities:
 * - Retrieve authenticated user information
 * - Update user profile (username, email, password)
 * - Validate email uniqueness during updates
 * - Extract authenticated user from security context
 * - Map user entities to response DTOs
 * 
 * All operations automatically use the authenticated user from the security context,
 * ensuring users can only access and modify their own profiles.
 * 
 * @author Cécile UMECKER
 
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * Retrieves the profile information of the currently authenticated user.
   * 
   * This method extracts the user ID from the security context, fetches the
   * corresponding user entity, and returns it as a DTO containing profile details.
   * 
   * @return UserResponseDTO containing the current user's profile information
   * @throws ResponseStatusException with 401 status if user is not found
   */
  public UserResponseDTO getCurrentUser() {
      User user = getAuthenticatedUser();
      return mapToUserResponseDTO(user);
  }

  /**
   * Updates the profile information of the currently authenticated user.
   * 
   * This method allows users to update their username, email, and/or password.
   * Only non-null and non-blank values are processed. Email updates are validated
   * to ensure uniqueness. Passwords are encrypted using BCrypt before storage.
   * 
   * @param dto the update request containing new username, email, and/or password
   * @return UserResponseDTO containing the updated user profile information
   * @throws ResponseStatusException with 409 status if email is already in use
   * @throws ResponseStatusException with 401 status if user is not found
   */
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

  /**
   * Retrieves the authenticated user entity from the database.
   * 
   * This method extracts the user ID from the Spring Security context and fetches
   * the corresponding user entity. It is used internally by other service methods
   * to obtain the current user for various operations.
   * 
   * @return User entity representing the authenticated user
   * @throws ResponseStatusException with 401 status if user is not found
   */
  User getAuthenticatedUser() {
    String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
    Long userId = Long.valueOf(userIdStr);

    return userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "User not found"
            ));
  }

  /**
   * Maps a User entity to a UserResponseDTO.
   * 
   * This method transforms a user entity into a response DTO containing only
   * the information that should be exposed through the API (username and email).
   * Sensitive information like passwords is excluded.
   * 
   * @param user the User entity to map
   * @return UserResponseDTO containing user profile information
   */
  private UserResponseDTO mapToUserResponseDTO(User user) {
      UserResponseDTO dto = new UserResponseDTO();
      dto.setUsername(user.getUsername());
      dto.setEmail(user.getEmail());
      return dto;
  }
}
