package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.*;
import com.openclassrooms.mddapi.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller handling user-related endpoints for the MDD API.
 * 
 * This controller provides RESTful endpoints for managing user profiles and
 * account information. It allows authenticated users to retrieve and update
 * their own profile data.
 * 
 * Key functionalities:
 * - Retrieve current user's profile information
 * - Update current user's profile (username, email, password)
 * 
 * All endpoints under this controller require authentication and are mapped
 * under the "/user" path. Users can only access and modify their own profile.
 * 
 * @author Cécile UMECKER
 
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Retrieves the profile information of the currently authenticated user.
     * 
     * This endpoint returns the profile details of the user making the request,
     * including their username, email, and other account information. The user
     * is identified through their JWT authentication token.
     * 
     * @return ResponseEntity containing the UserResponseDTO with current user's profile (200 OK)
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        UserResponseDTO user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    /**
     * Updates the profile information of the currently authenticated user.
     * 
     * This endpoint allows users to modify their profile details such as username,
     * email address, and password. The user is identified through their JWT authentication token.
     * The system validates that the new email is unique before applying the update.
     * 
     * @param userUpdateRequestDTO the update request containing new username, email, and/or password
     * @return ResponseEntity containing the updated UserResponseDTO (200 OK)
     */
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateCurrentUser(
            @RequestBody UserUpdateRequestDTO userUpdateRequestDTO
    ) {
        UserResponseDTO updatedUser = userService.updateCurrentUser(userUpdateRequestDTO);
        return ResponseEntity.ok(updatedUser);
    }
}
