package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for user registration.
 * This class encapsulates the data required for registering a new user.
 * 
 * The class uses Lombok @Data annotation to automatically generate:
 * - getters and setters
 * - equals() and hashCode() methods
 * - toString() method
 * 
 * Fields:
 * @property username Required field for user's username
 * @property email Required field for user's email address (must be valid email format)
 * @property password Required field for user's password
 * 
 * All fields are validated using Jakarta validation constraints:
 * - @NotBlank ensures fields are not empty or null
 * - @Email ensures email field contains valid email format
 */

@Data
public class RegisterDTO {

  @NotBlank(message = "username must not be blank")
  @JsonProperty("username")
  private String username;

  @Email(message = "email")
  @NotBlank(message = "email must not be blank")
  private String email;

  @NotBlank(message = "password must not be blank")
  private String password;
}
