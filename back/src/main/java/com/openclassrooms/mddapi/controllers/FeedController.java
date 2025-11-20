package com.openclassrooms.mddapi.controllers;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.mddapi.dto.FeedPostDTO;
import com.openclassrooms.mddapi.services.FeedService;

import lombok.RequiredArgsConstructor;

/**
 * Controller handling feed-related endpoints for the MDD API.
 * 
 * This controller provides endpoints for retrieving personalized content feeds
 * for authenticated users. The feed displays posts from topics that the user
 * has subscribed to, enabling users to stay updated with relevant content.
 * 
 * All endpoints under this controller require authentication and are mapped
 * under the "/feed" path. Results are paginated for optimal performance.
 * 
 * @author Cécile UMECKER
 */
@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    /**
     * Retrieves a paginated feed of posts for the authenticated user.
     * 
     * This endpoint returns posts from topics that the current user has subscribed to,
     * sorted by creation date (newest first) by default. The results are paginated to improve
     * performance and user experience when dealing with large amounts of content.
     * 
     * @param page the page number to retrieve (zero-based index), defaults to 0
     * @param size the number of items per page, defaults to 10
     * @param sort optional sort parameter in format "field,direction" (e.g., "createdAt,desc")
     * @return a Page containing FeedPostDTO objects with post details and metadata
     */
    @GetMapping
    public Page<FeedPostDTO> getFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort) {
        return feedService.getFeed(page, size, sort);
    }
}
