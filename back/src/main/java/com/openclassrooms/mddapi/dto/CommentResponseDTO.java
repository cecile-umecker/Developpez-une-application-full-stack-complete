package com.openclassrooms.mddapi.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponseDTO {
    private Long id;
    private String authorName;
    private String content;
    private LocalDateTime createdAt;
}
