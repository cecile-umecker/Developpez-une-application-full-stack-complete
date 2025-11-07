package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.*;
import com.openclassrooms.mddapi.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
        summary = "Get current user info",
        description = "Returns the username and email of the currently authenticated user",
        responses = {
            @ApiResponse(responseCode = "200", description = "User found",
                content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, token missing or invalid")
        }
    )
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        UserResponseDTO user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    @Operation(
        summary = "Update current user info",
        description = "Updates the username, email or password of the currently authenticated user. Fields are optional.",
        responses = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, token missing or invalid"),
            @ApiResponse(responseCode = "409", description = "Conflict, email already in use")
        }
    )
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateCurrentUser(
            @RequestBody UserUpdateRequestDTO userUpdateRequestDTO
    ) {
        UserResponseDTO updatedUser = userService.updateCurrentUser(userUpdateRequestDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(
        summary = "Get subscribed topics",
        description = "Returns a list of topics the currently authenticated user is subscribed to",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of topics retrieved successfully",
                content = @Content(schema = @Schema(implementation = TopicResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, token missing or invalid")
        }
    )
    @GetMapping("/me/subscriptions")
    public ResponseEntity<List<TopicResponseDTO>> getUserSubscriptions() {
        List<TopicResponseDTO> subscriptions = userService.getUserSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }
}
