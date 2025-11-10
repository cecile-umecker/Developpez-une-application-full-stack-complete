package com.openclassrooms.mddapi.dto;

import lombok.*;

/**
 * Data Transfer Object for user profile update requests.
 * 
 * This DTO encapsulates the data that can be updated in a user's profile,
 * including username, email, and password.
 * 
 * @author Cécile UMECKER
 
 */
@Data
public class UserUpdateRequestDTO {
  
    private String username;
    private String email;
    private String password;
}
