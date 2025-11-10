package com.openclassrooms.mddapi.dto;

import lombok.*;

/**
 * Data Transfer Object for user profile responses.
 * 
 * This DTO encapsulates user information returned to clients,
 * excluding sensitive data like passwords.
 * 
 * @author Cécile UMECKER
 
 */
@Data
public class UserResponseDTO {

    private String username;
    private String email;
}
