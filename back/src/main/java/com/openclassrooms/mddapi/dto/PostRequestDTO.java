package com.openclassrooms.mddapi.dto;

import lombok.Data;

@Data
public class PostRequestDTO {
    private String title;
    private String content;
    private Long topicId;
}
