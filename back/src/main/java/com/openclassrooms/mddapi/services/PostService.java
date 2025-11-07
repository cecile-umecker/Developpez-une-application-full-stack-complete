package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.*;
import com.openclassrooms.mddapi.models.*;
import com.openclassrooms.mddapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TopicRepository topicRepository;
    private final UserService userService;

    /** GET détail d’un post */
    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return mapToPostResponseDTO(post);
    }

    /** POST écrire un post */
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

    /** GET récupère les commentaires d’un post */
    public Page<CommentResponseDTO> getCommentsByPost(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        return commentRepository.findByPostOrderByCreatedAtAsc(post, pageable)
                .map(this::mapToCommentResponseDTO);
    }

    /** POST écrire un commentaire sur un post */
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

    /** Mappers **/
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

    private CommentResponseDTO mapToCommentResponseDTO(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .authorName(comment.getUser().getUsername())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
