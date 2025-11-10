package com.openclassrooms.mddapi.dto;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * Data Transfer Object for feed post items.
 * 
 * This DTO represents a simplified post view in the user's personalized feed,
 * containing essential information for displaying posts in a list or summary format.
 * 
 * @author Cécile UMECKER
 
 */
@Data
public class FeedPostDTO {
    private Long id;
    private String title;
    private String authorName;
    private String contentPreview;
    private LocalDateTime createdAt;
}
