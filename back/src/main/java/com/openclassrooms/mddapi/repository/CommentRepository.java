package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.models.Post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Comment entity database operations.
 * 
 * This repository extends JpaRepository to provide standard CRUD operations
 * for Comment entities, along with custom query methods for retrieving comments
 * associated with specific posts.
 * 
 * Custom query methods allow retrieving comments ordered by creation date,
 * supporting chronological discussion flows. Results are paginated for
 * optimal performance with posts that have many comments.
 * 
 * @author Cécile UMECKER
 
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
  
  /**
   * Retrieves all comments for a specific post, ordered by creation date ascending.
   * 
   * This method fetches comments in chronological order (oldest first), allowing
   * users to follow the discussion flow naturally. Results are paginated to handle
   * posts with large numbers of comments efficiently.
   * 
   * @param post the Post entity to retrieve comments for
   * @param pageable pagination parameters (page number, size, sorting)
   * @return Page containing Comment entities ordered by creation date
   */
  List<Comment> findByPostOrderByCreatedAtAsc(Post post);
}
