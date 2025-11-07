package com.openclassrooms.mddapi.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FeedPostDTO {
    private Long id;
    private String title;
    private String authorName;
    private String contentPreview;
    private LocalDateTime createdAt;
}
