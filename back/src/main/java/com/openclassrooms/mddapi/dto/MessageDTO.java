package com.openclassrooms.mddapi.dto;

import lombok.*;

/**
 * Data Transfer Object (DTO) for message-related operations.
 * This class is used to transfer message data between different layers of the application.
 * 
 * The class uses Lombok annotations to automatically generate:
 * - Getters and setters (@Data)
 * - All-args constructor (@AllArgsConstructor)
 * - No-args constructor (@NoArgsConstructor)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private String message;
}
