package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

  @NotBlank(message = "Login must not be blank")
  private String login;
  @NotBlank(message = "Password must not be blank")
  private String password;
}
