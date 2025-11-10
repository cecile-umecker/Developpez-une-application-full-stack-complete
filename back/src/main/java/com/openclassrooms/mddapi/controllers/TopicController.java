package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.TopicWithSubscriptionDTO;
import com.openclassrooms.mddapi.services.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller handling topic-related endpoints for the MDD API.
 * 
 * This controller provides RESTful endpoints for managing topics and user subscriptions
 * to topics. Topics serve as categories for organizing posts, and users can subscribe
 * to topics to receive relevant content in their personalized feed.
 * 
 * Key functionalities:
 * - Retrieve all available topics with subscription status
 * - Retrieve topics the current user is subscribed to
 * - Subscribe to topics
 * - Unsubscribe from topics
 * 
 * All endpoints under this controller require authentication and are mapped
 * under the "/topic" path.
 * 
 * @author Cécile UMECKER
 
 */
@RestController
@RequestMapping("/topic")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    /**
     * Retrieves all available topics with subscription status for the current user.
     * 
     * This endpoint returns a complete list of all topics in the system, with each
     * topic indicating whether the authenticated user is currently subscribed to it.
     * This allows the UI to display all topics with appropriate subscription indicators.
     * 
     * @return ResponseEntity containing a list of TopicWithSubscriptionDTO objects (200 OK)
     */
    @GetMapping
    public ResponseEntity<List<TopicWithSubscriptionDTO>> getAllTopics() {
        List<TopicWithSubscriptionDTO> topics = topicService.getAllTopicsWithSubscriptionFlag();
        return ResponseEntity.ok(topics);
    }

    /**
     * Retrieves all topics that the current user is subscribed to.
     * 
     * This endpoint returns only the topics to which the authenticated user has
     * an active subscription. These subscribed topics determine which posts appear
     * in the user's personalized feed.
     * 
     * @return ResponseEntity containing a list of subscribed TopicWithSubscriptionDTO objects (200 OK)
     */
    @GetMapping("/my")
    public ResponseEntity<List<TopicWithSubscriptionDTO>> getMyTopics() {
        List<TopicWithSubscriptionDTO> topics = topicService.getUserSubscribedTopics();
        return ResponseEntity.ok(topics);
    }

    /**
     * Subscribes the current user to a specific topic.
     * 
     * This endpoint creates a subscription relationship between the authenticated user
     * and the specified topic. Once subscribed, posts from this topic will appear in
     * the user's feed. If the user is already subscribed, this operation is idempotent.
     * 
     * @param id the unique identifier of the topic to subscribe to
     * @return ResponseEntity containing the updated TopicWithSubscriptionDTO with subscription status (200 OK)
     */
    @PostMapping("/{id}/subscription")
    public ResponseEntity<TopicWithSubscriptionDTO> subscribeToTopic(@PathVariable Long id) {
        TopicWithSubscriptionDTO updatedTopic = topicService.subscribeToTopic(id);
        return ResponseEntity.ok(updatedTopic);
    }

    /**
     * Unsubscribes the current user from a specific topic.
     * 
     * This endpoint removes the subscription relationship between the authenticated user
     * and the specified topic. After unsubscribing, posts from this topic will no longer
     * appear in the user's feed. If the user is not subscribed, this operation is idempotent.
     * 
     * @param id the unique identifier of the topic to unsubscribe from
     * @return ResponseEntity containing the updated TopicWithSubscriptionDTO with subscription status (200 OK)
     */
    @DeleteMapping("/{id}/subscription")
    public ResponseEntity<TopicWithSubscriptionDTO> unsubscribeFromTopic(@PathVariable Long id) {
        TopicWithSubscriptionDTO updatedTopic = topicService.unsubscribeFromTopic(id);
        return ResponseEntity.ok(updatedTopic);
    }
}
