package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.TopicWithSubscriptionDTO;
import com.openclassrooms.mddapi.models.Topic;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Service responsible for managing topic and subscription operations in the MDD API.
 * 
 * This service provides business logic for managing topics and user subscriptions to
 * topics. It handles topic retrieval with subscription status indicators and manages
 * the many-to-many relationship between users and topics.
 * 
 * Key responsibilities:
 * - Retrieve all topics with user-specific subscription flags
 * - Retrieve topics the current user is subscribed to
 * - Subscribe users to topics
 * - Unsubscribe users from topics
 * - Map topic entities to DTOs with subscription status
 * 
 * All operations automatically use the authenticated user from the security context
 * to determine subscription status and manage subscriptions.
 * 
 * @author Cécile UMECKER
 
 */
@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * Retrieves all available topics with subscription status for the current user.
     * 
     * This method fetches all topics in the system and includes a subscription flag
     * for each topic indicating whether the authenticated user is currently subscribed.
     * This allows the UI to display all topics with appropriate subscription indicators.
     * 
     * @return List of TopicWithSubscriptionDTO objects with subscription status
     */
    public List<TopicWithSubscriptionDTO> getAllTopicsWithSubscriptionFlag() {
        User user = userService.getAuthenticatedUser();
        List<Long> subscribedTopicIds = user.getTopics().stream()
                                           .map(Topic::getId)
                                           .toList();

        return topicRepository.findAll().stream()
                .map(topic -> mapToTopicWithSubscriptionDTO(topic, subscribedTopicIds.contains(topic.getId())))
                .toList();
    }

    /**
     * Retrieves all topics that the current user is subscribed to.
     * 
     * This method returns only the topics to which the authenticated user has an
     * active subscription. These subscribed topics determine which posts appear in
     * the user's personalized feed. All returned topics have their subscription
     * flag set to true.
     * 
     * @return List of TopicWithSubscriptionDTO objects for subscribed topics
     */
    public List<TopicWithSubscriptionDTO> getUserSubscribedTopics() {
        User user = userService.getAuthenticatedUser();

        return user.getTopics().stream()
                .map(topic -> mapToTopicWithSubscriptionDTO(topic, true))
                .toList();
    }

    /**
     * Subscribes the current user to a specific topic.
     * 
     * This method creates a subscription relationship between the authenticated user
     * and the specified topic. Once subscribed, posts from this topic will appear in
     * the user's feed. If the user is already subscribed, this operation is idempotent
     * and will not create duplicate subscriptions.
     * 
     * @param topicId the unique identifier of the topic to subscribe to
     * @return TopicWithSubscriptionDTO with subscription flag set to true
     * @throws ResponseStatusException with 404 status if topic is not found
     */
    public TopicWithSubscriptionDTO subscribeToTopic(Long topicId) {
        User user = userService.getAuthenticatedUser();
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));

        if (!user.getTopics().contains(topic)) {
            user.getTopics().add(topic);
            userRepository.save(user);
        }

        return mapToTopicWithSubscriptionDTO(topic, true);
    }

    /**
     * Unsubscribes the current user from a specific topic.
     * 
     * This method removes the subscription relationship between the authenticated user
     * and the specified topic. After unsubscribing, posts from this topic will no longer
     * appear in the user's feed. If the user is not subscribed, this operation is
     * idempotent and will not raise an error.
     * 
     * @param topicId the unique identifier of the topic to unsubscribe from
     * @return TopicWithSubscriptionDTO with subscription flag set to false
     * @throws ResponseStatusException with 404 status if topic is not found
     */
    public TopicWithSubscriptionDTO unsubscribeFromTopic(Long topicId) {
        User user = userService.getAuthenticatedUser();
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));

        boolean removed = user.getTopics().remove(topic);
        if (removed) {
            userRepository.save(user);
        }

        return mapToTopicWithSubscriptionDTO(topic, false);
    }

    /**
     * Maps a Topic entity to a TopicWithSubscriptionDTO with subscription status.
     * 
     * This method transforms a topic entity into a response DTO containing topic
     * information (ID, title, description) along with a subscription flag indicating
     * whether the current user is subscribed to this topic.
     * 
     * @param topic the Topic entity to map
     * @param subscribed the subscription status flag for the current user
     * @return TopicWithSubscriptionDTO containing topic information and subscription status
     */
    private TopicWithSubscriptionDTO mapToTopicWithSubscriptionDTO(Topic topic, boolean subscribed) {
        TopicWithSubscriptionDTO dto = new TopicWithSubscriptionDTO();
        dto.setId(topic.getId());
        dto.setTitle(topic.getTitle());
        dto.setDescription(topic.getDescription());
        dto.setSubscribed(subscribed);
        return dto;
    }
}
