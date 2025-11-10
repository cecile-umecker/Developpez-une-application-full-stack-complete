package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object for user login requests.
 * 
 * This DTO encapsulates the credentials required for user authentication.
 * The login field accepts either username or email for flexible authentication.
 * 
 * @author Cécile UMECKER
 
 */
@Data
public class LoginDTO {

  @NotBlank(message = "Login must not be blank")
  private String login;
  @NotBlank(message = "Password must not be blank")
  private String password;
}
