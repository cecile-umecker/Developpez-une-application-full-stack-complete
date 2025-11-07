package com.openclassrooms.mddapi.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private String topicTitle;
    private LocalDateTime createdAt;
}
