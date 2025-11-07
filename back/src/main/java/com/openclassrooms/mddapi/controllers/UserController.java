package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.*;
import com.openclassrooms.mddapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET /me
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        UserResponseDTO user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    // PUT /me
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateCurrentUser(
            @RequestBody UserUpdateRequestDTO userUpdateRequestDTO
    ) {
        UserResponseDTO updatedUser = userService.updateCurrentUser(userUpdateRequestDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // GET /me/subscriptions
    @GetMapping("/me/subscriptions")
    public ResponseEntity<List<TopicResponseDTO>> getUserSubscriptions() {
        List<TopicResponseDTO> subscriptions = userService.getUserSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }
}
