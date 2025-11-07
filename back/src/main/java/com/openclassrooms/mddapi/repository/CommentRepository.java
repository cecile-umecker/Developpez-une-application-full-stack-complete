package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.models.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  Page<Comment> findByPostOrderByCreatedAtAsc(Post post, Pageable pageable);
}
