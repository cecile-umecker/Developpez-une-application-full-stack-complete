package com.openclassrooms.mddapi.dto;

import lombok.Data;

/**
 * Data Transfer Object for comment creation requests.
 * 
 * This DTO encapsulates the data required to create a new comment on a post.
 * It is used as the request body in the comment creation endpoint.
 * 
 * The actual post ID and user information are derived from the request context
 * (URL path parameter and authenticated user) rather than being included in this DTO.
 * 
 * @author Cécile UMECKER
 
 */
@Data
public class CommentRequestDTO {
    private String content;
}
