package com.openclassrooms.mddapi.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.mddapi.dto.FeedPostDTO;
import com.openclassrooms.mddapi.models.Post;
import com.openclassrooms.mddapi.models.Topic;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.PostRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service responsible for managing user feed operations in the MDD API.
 * 
 * This service provides functionality for retrieving personalized content feeds
 * for authenticated users. The feed displays posts from topics that the user has
 * subscribed to, allowing users to stay updated with relevant content.
 * 
 * Key responsibilities:
 * - Generate personalized feeds based on user subscriptions
 * - Retrieve posts from subscribed topics with pagination
 * - Map post entities to feed DTOs with content previews
 * - Truncate long content for preview display
 * 
 * Posts in the feed are limited to those from topics the user has subscribed to,
 * and content is truncated to 150 characters for preview purposes.
 * 
 * @author Cécile UMECKER
 
 */
@Service
@RequiredArgsConstructor
public class FeedService {

    private final PostRepository postRepository;
    private final UserService userService;

    private static final int CONTENT_PREVIEW_LENGTH = 150;

    /**
     * Retrieves a paginated feed of posts for the authenticated user.
     * 
     * This method generates a personalized feed by fetching posts from all topics
     * that the current user has subscribed to. Posts are returned in paginated format
     * with content previews for better performance and user experience.
     * 
     * @param page the page number to retrieve (zero-based index)
     * @param size the number of posts per page
     * @return a Page containing FeedPostDTO objects with post previews
     */
    public Page<FeedPostDTO> getFeed(int page, int size, @RequestParam(required = false) String sort) {
        User user = userService.getAuthenticatedUser();
        List<Long> subscribedTopicIds = user.getTopics().stream()
                                        .map(Topic::getId)
                                        .toList();

        Pageable pageable;
        if (sort != null && !sort.isEmpty()) {
            String[] parts = sort.split(",");
            Sort.Direction direction = Sort.Direction.fromString(parts[1]);
            pageable = PageRequest.of(page, size, Sort.by(direction, parts[0]));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        return postRepository.findByTopicIdIn(subscribedTopicIds, pageable)
                            .map(this::mapToDTO);
    }

    /**
     * Maps a Post entity to a FeedPostDTO with preview information.
     * 
     * This method transforms a full post entity into a lightweight DTO suitable
     * for feed display. It includes the post ID, title, author name, creation date,
     * and a truncated content preview.
     * 
     * @param post the Post entity to map
     * @return a FeedPostDTO containing post preview information
     */
    private FeedPostDTO mapToDTO(Post post) {
        FeedPostDTO dto = new FeedPostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setAuthorName(post.getUser().getUsername());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setContentPreview(truncateContent(post.getContent()));
        return dto;
    }

    /**
     * Truncates content to a preview length for feed display.
     * 
     * This method limits content to a maximum of 150 characters for preview purposes.
     * If the content exceeds this length, it is trimmed and appended with "...".
     * If content is null, an empty string is returned.
     * 
     * @param content the full content text to truncate
     * @return the truncated content with ellipsis if needed, or empty string if null
     */
    private String truncateContent(String content) {
        if (content == null) return "";
        if (content.length() <= CONTENT_PREVIEW_LENGTH) return content;
        return content.substring(0, CONTENT_PREVIEW_LENGTH).trim() + "...";
    }
}

