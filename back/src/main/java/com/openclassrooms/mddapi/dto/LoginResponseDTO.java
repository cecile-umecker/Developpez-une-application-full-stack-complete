package com.openclassrooms.mddapi.dto;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

  private String accessToken;
  private String refreshToken;
  private String message;
}
