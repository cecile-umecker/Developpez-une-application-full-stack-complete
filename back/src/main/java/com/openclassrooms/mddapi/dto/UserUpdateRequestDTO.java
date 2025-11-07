package com.openclassrooms.mddapi.dto;

import lombok.*;

@Data
public class UserUpdateRequestDTO {
  
    private String username;
    private String email;
    private String password;
}
