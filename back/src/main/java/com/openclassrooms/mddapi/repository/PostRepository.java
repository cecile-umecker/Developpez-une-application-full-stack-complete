package com.openclassrooms.mddapi.repository;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import com.openclassrooms.mddapi.models.Post;

/**
 * Repository interface for Post entity database operations.
 * 
 * This repository extends JpaRepository to provide standard CRUD operations
 * for Post entities, along with custom query methods for retrieving posts
 * from specific topics.
 * 
 * Custom query methods support feed generation by allowing retrieval of posts
 * from multiple topics simultaneously, which is essential for creating
 * personalized user feeds based on topic subscriptions.
 * 
 * @author Cécile UMECKER
 
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Retrieves all posts belonging to any of the specified topics.
     * 
     * This method fetches posts from multiple topics in a single query,
     * which is particularly useful for generating personalized feeds based
     * on a user's topic subscriptions. Results are paginated for optimal
     * performance when dealing with large numbers of posts.
     * 
     * @param topicIds list of topic IDs to retrieve posts from
     * @param pageable pagination parameters (page number, size, sorting)
     * @return Page containing Post entities from the specified topics
     */
    Page<Post> findByTopicIdIn(List<Long> topicIds, Pageable pageable);

}
