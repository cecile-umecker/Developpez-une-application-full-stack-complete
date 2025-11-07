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

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    /** Get all topics with a flag for the authenticated user */
    public List<TopicWithSubscriptionDTO> getAllTopicsWithSubscriptionFlag() {
        User user = userService.getAuthenticatedUser();
        List<Long> subscribedTopicIds = user.getTopics().stream()
                                           .map(Topic::getId)
                                           .toList();

        return topicRepository.findAll().stream()
                .map(topic -> mapToTopicWithSubscriptionDTO(topic, subscribedTopicIds.contains(topic.getId())))
                .toList();
    }

    /** Get only topics the user is subscribed to */
    public List<TopicWithSubscriptionDTO> getUserSubscribedTopics() {
        User user = userService.getAuthenticatedUser();

        return user.getTopics().stream()
                .map(topic -> mapToTopicWithSubscriptionDTO(topic, true))
                .toList();
    }

    /** Subscribe user to a topic and return updated list of topics with flags */
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

    /** Unsubscribe user from a topic and return updated list of topics with flags */
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

    private TopicWithSubscriptionDTO mapToTopicWithSubscriptionDTO(Topic topic, boolean subscribed) {
        TopicWithSubscriptionDTO dto = new TopicWithSubscriptionDTO();
        dto.setId(topic.getId());
        dto.setTitle(topic.getTitle());
        dto.setDescription(topic.getDescription());
        dto.setSubscribed(subscribed);
        return dto;
    }
}
