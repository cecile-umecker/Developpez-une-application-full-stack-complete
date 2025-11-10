package com.openclassrooms.mddapi.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for comment responses.
 * 
 * This DTO encapsulates comment data returned to clients, including
 * the comment content, author information, and timestamps.
 * 
 * @author Cécile UMECKER
 
 */
@Data
@Builder
public class CommentResponseDTO {
    private Long id;
    private String authorName;
    private String content;
    private LocalDateTime createdAt;
}
