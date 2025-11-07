package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.TopicWithSubscriptionDTO;
import com.openclassrooms.mddapi.services.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topic")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    // GET /topic
    @GetMapping
    public ResponseEntity<List<TopicWithSubscriptionDTO>> getAllTopics() {
        List<TopicWithSubscriptionDTO> topics = topicService.getAllTopicsWithSubscriptionFlag();
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/my")
    public ResponseEntity<List<TopicWithSubscriptionDTO>> getMyTopics() {
        List<TopicWithSubscriptionDTO> topics = topicService.getUserSubscribedTopics();
        return ResponseEntity.ok(topics);
    }

    // POST /topic/{id}/subscription
    @PostMapping("/{id}/subscription")
    public ResponseEntity<TopicWithSubscriptionDTO> subscribeToTopic(@PathVariable Long id) {
        TopicWithSubscriptionDTO updatedTopic = topicService.subscribeToTopic(id);
        return ResponseEntity.ok(updatedTopic);
    }

    // DELETE /topic/{id}/subscription
    @DeleteMapping("/{id}/subscription")
    public ResponseEntity<TopicWithSubscriptionDTO> unsubscribeFromTopic(@PathVariable Long id) {
        TopicWithSubscriptionDTO updatedTopic = topicService.unsubscribeFromTopic(id);
        return ResponseEntity.ok(updatedTopic);
    }
}
