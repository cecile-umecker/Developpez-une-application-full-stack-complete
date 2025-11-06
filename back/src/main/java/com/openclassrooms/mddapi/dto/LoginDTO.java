package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for handling login credentials.
 * This class is used to transfer login information between client and server.
 * 
 * The class uses Lombok's @Data annotation to automatically generate:
 * - getters
 * - setters
 * - toString
 * - equals
 * - hashCode
 */

@Data
public class LoginDTO {

  @NotBlank(message = "Login must not be blank")
  private String login;
  @NotBlank(message = "Password must not be blank")
  private String password;
}
