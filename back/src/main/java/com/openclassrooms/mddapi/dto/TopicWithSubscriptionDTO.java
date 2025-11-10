package com.openclassrooms.mddapi.dto;

import lombok.Data;

/**
 * Data Transfer Object for topic responses with subscription status.
 * 
 * This DTO encapsulates topic information along with a flag indicating
 * whether the current user is subscribed to the topic.
 * 
 * @author Cécile UMECKER
 
 */
@Data
public class TopicWithSubscriptionDTO {
    private Long id;
    private String title;
    private String description;
    private boolean subscribed;
}