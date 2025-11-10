package com.openclassrooms.mddapi.dto;

import lombok.Data;

/**
 * Data Transfer Object for post creation requests.
 * 
 * This DTO encapsulates the data required to create a new post,
 * including the title, content, and the topic it belongs to.
 * 
 * @author Cécile UMECKER
 
 */
@Data
public class PostRequestDTO {
    private String title;
    private String content;
    private Long topicId;
}
