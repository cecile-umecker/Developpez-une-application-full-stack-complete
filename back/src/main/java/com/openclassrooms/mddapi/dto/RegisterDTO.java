package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

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
