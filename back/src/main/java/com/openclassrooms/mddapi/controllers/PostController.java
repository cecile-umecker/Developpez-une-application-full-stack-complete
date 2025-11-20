package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.CommentRequestDTO;
import com.openclassrooms.mddapi.dto.CommentResponseDTO;
import com.openclassrooms.mddapi.dto.PostRequestDTO;
import com.openclassrooms.mddapi.dto.PostResponseDTO;
import com.openclassrooms.mddapi.services.PostService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller handling post-related endpoints for the MDD API.
 * 
 * This controller provides RESTful endpoints for managing posts and their associated
 * comments. Posts are the main content items created by users within topics, allowing
 * for discussions and information sharing.
 * 
 * Key functionalities:
 * - Retrieve individual posts by ID
 * - Create new posts within topics
 * - Retrieve comments for a specific post
 * - Add comments to posts
 * 
 * All endpoints under this controller require authentication and are mapped
 * under the "/post" path.
 * 
 * @author Cécile UMECKER
 
 */
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * Retrieves a specific post by its unique identifier.
     * 
     * This endpoint fetches detailed information about a post including its title,
     * content, author, topic, and creation date.
     * 
     * @param id the unique identifier of the post to retrieve
     * @return ResponseEntity containing the PostResponseDTO with post details (200 OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        PostResponseDTO post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    /**
     * Creates a new post within a specific topic.
     * 
     * This endpoint allows authenticated users to create new posts with a title
     * and content, associated with a particular topic. The post is automatically
     * linked to the authenticated user as the author.
     * 
     * @param postRequest the post creation request containing title, content, and topic ID
     * @return ResponseEntity containing the created PostResponseDTO (200 OK)
     */
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostRequestDTO postRequest) {
        PostResponseDTO post = postService.createPost(postRequest);
        return ResponseEntity.ok(post);
    }

    /**
     * Retrieves all comments for a specific post.
     * 
     * This endpoint returns a list of all comments associated with the specified
     * post. Comments are ordered by creation date, allowing users to follow the
     * discussion chronologically. No pagination is applied.
     * 
     * @param id the unique identifier of the post
     * @return ResponseEntity containing a List of CommentResponseDTO objects (200 OK)
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByPost(@PathVariable Long id) {
        List<CommentResponseDTO> comments = postService.getCommentsByPost(id);
        return ResponseEntity.ok(comments);
    }

    /**
     * Adds a new comment to a specific post.
     * 
     * This endpoint allows authenticated users to add comments to existing posts,
     * facilitating discussions and feedback. The comment is automatically linked
     * to the authenticated user as the author and the specified post.
     * 
     * @param id the unique identifier of the post to comment on
     * @param commentRequest the comment creation request containing the comment content
     * @return ResponseEntity containing the created CommentResponseDTO (200 OK)
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponseDTO> addCommentToPost(
            @PathVariable Long id,
            @RequestBody CommentRequestDTO commentRequest) {
        CommentResponseDTO comment = postService.addCommentToPost(id, commentRequest);
        return ResponseEntity.ok(comment);
    }
}
