package com.openclassrooms.mddapi.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for post responses.
 * 
 * This DTO encapsulates complete post data returned to clients,
 * including content, author information, topic details, and timestamps.
 * 
 * @author Cécile UMECKER
 
 */
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
