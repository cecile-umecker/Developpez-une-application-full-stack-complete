package com.openclassrooms.mddapi.dto;

import lombok.Data;

@Data
public class TopicWithSubscriptionDTO {
    private Long id;
    private String title;
    private String description;
    private boolean subscribed;
}