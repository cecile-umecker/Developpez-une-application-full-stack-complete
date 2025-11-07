package com.openclassrooms.mddapi.controllers;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.mddapi.dto.FeedPostDTO;
import com.openclassrooms.mddapi.services.FeedService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping
    public Page<FeedPostDTO> getFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return feedService.getFeed(page, size);
    }
}
