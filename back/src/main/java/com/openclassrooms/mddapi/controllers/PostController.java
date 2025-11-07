package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.CommentRequestDTO;
import com.openclassrooms.mddapi.dto.CommentResponseDTO;
import com.openclassrooms.mddapi.dto.PostRequestDTO;
import com.openclassrooms.mddapi.dto.PostResponseDTO;
import com.openclassrooms.mddapi.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // GET détail d’un post ⇒ /api/post/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        PostResponseDTO post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    // POST écrire un post ⇒ /api/post
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostRequestDTO postRequest) {
        PostResponseDTO post = postService.createPost(postRequest);
        return ResponseEntity.ok(post);
    }

    // GET récupère les commentaires d’un post ⇒ /api/post/{id}/comments
    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsByPost(
            @PathVariable Long id,
            Pageable pageable) {
        Page<CommentResponseDTO> comments = postService.getCommentsByPost(id, pageable);
        return ResponseEntity.ok(comments);
    }

    // POST écrire un commentaire sur un post ⇒ /api/post/{id}/comments
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponseDTO> addCommentToPost(
            @PathVariable Long id,
            @RequestBody CommentRequestDTO commentRequest) {
        CommentResponseDTO comment = postService.addCommentToPost(id, commentRequest);
        return ResponseEntity.ok(comment);
    }
}
