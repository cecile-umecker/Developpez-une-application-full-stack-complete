package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.*;
import com.openclassrooms.mddapi.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        UserResponseDTO user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateCurrentUser(
            @RequestBody UserUpdateRequestDTO userUpdateRequestDTO
    ) {
        UserResponseDTO updatedUser = userService.updateCurrentUser(userUpdateRequestDTO);
        return ResponseEntity.ok(updatedUser);
    }
}
