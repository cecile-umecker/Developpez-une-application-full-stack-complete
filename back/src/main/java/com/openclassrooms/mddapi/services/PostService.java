package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.*;
import com.openclassrooms.mddapi.models.*;
import com.openclassrooms.mddapi.repository.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

/**
 * Service responsible for managing post and comment operations in the MDD API.
 * 
 * This service provides business logic for creating and retrieving posts, as well as
 * managing comments on posts. It handles the relationships between posts, users, topics,
 * and comments, ensuring proper validation and data integrity.
 * 
 * Key responsibilities:
 * - Retrieve posts by ID with full details
 * - Create new posts within topics
 * - Retrieve paginated comments for posts
 * - Add comments to posts
 * - Map entities to response DTOs
 * 
 * All operations requiring user context automatically use the authenticated user
 * from the security context.
 * 
 * @author Cécile UMECKER
 
 */
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TopicRepository topicRepository;
    private final UserService userService;

    /**
     * Retrieves a post by its unique identifier.
     * 
     * This method fetches a post with all its details including title, content,
     * author information, topic, and creation date.
     * 
     * @param id the unique identifier of the post to retrieve
     * @return PostResponseDTO containing the complete post information
     * @throws ResponseStatusException with 404 status if post is not found
     */
    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return mapToPostResponseDTO(post);
    }

    /**
     * Creates a new post within a specified topic.
     * 
     * This method creates a post with the provided title and content, associates it
     * with the specified topic, and automatically links it to the authenticated user
     * as the author. The post is persisted to the database with its creation timestamp.
     * 
     * @param postRequest the post creation request containing title, content, and topic ID
     * @return PostResponseDTO containing the created post information
     * @throws ResponseStatusException with 404 status if topic is not found
     */
    public PostResponseDTO createPost(PostRequestDTO postRequest) {
        User user = userService.getAuthenticatedUser();
        Topic topic = topicRepository.findById(postRequest.getTopicId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));

        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .user(user)
                .topic(topic)
                .build();

        Post savedPost = postRepository.save(post);
        return mapToPostResponseDTO(savedPost);
    }

    /**
     * Retrieves all comments for a specific post.
     *
     * This method fetches all comments associated with the specified post,
     * ordered by creation date ascending, and returns them as a list.
     *
     * @param postId the unique identifier of the post
     * @return List of CommentResponseDTO
     * @throws ResponseStatusException with 404 status if post is not found
     */
     public List<CommentResponseDTO> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        return commentRepository.findByPostOrderByCreatedAtAsc(post)
                .stream()
                .map(this::mapToCommentResponseDTO)
                .toList();
     }

    /**
     * Adds a new comment to a specific post.
     * 
     * This method creates a comment with the provided content, associates it with
     * the specified post, and automatically links it to the authenticated user as
     * the author. The comment is persisted with its creation timestamp.
     * 
     * @param postId the unique identifier of the post to comment on
     * @param commentRequest the comment creation request containing the content
     * @return CommentResponseDTO containing the created comment information
     * @throws ResponseStatusException with 404 status if post is not found
     */
    public CommentResponseDTO addCommentToPost(Long postId, CommentRequestDTO commentRequest) {
        User user = userService.getAuthenticatedUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .post(post)
                .user(user)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return mapToCommentResponseDTO(savedComment);
    }

    /**
     * Maps a Post entity to a PostResponseDTO.
     * 
     * This method transforms a post entity into a response DTO containing all
     * relevant information for API responses, including post details, author name,
     * and topic title.
     * 
     * @param post the Post entity to map
     * @return PostResponseDTO containing formatted post information
     */
    private PostResponseDTO mapToPostResponseDTO(Post post) {
        return PostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(post.getUser().getUsername())
                .topicTitle(post.getTopic().getTitle())
                .createdAt(post.getCreatedAt())
                .build();
    }

    /**
     * Maps a Comment entity to a CommentResponseDTO.
     * 
     * This method transforms a comment entity into a response DTO containing all
     * relevant information for API responses, including comment content, author name,
     * and creation date.
     * 
     * @param comment the Comment entity to map
     * @return CommentResponseDTO containing formatted comment information
     */
    private CommentResponseDTO mapToCommentResponseDTO(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .authorName(comment.getUser().getUsername())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
