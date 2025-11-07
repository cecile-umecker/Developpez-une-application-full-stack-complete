package com.openclassrooms.mddapi.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.dto.FeedPostDTO;
import com.openclassrooms.mddapi.models.Post;
import com.openclassrooms.mddapi.models.Topic;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final PostRepository postRepository;
    private final UserService userService;

    private static final int CONTENT_PREVIEW_LENGTH = 150; // nombre de caractères pour le preview

    public Page<FeedPostDTO> getFeed(int page, int size) {
        User user = userService.getAuthenticatedUser();
        List<Long> subscribedTopicIds = user.getTopics().stream()
                                           .map(Topic::getId)
                                           .toList();

        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByTopicIdIn(subscribedTopicIds, pageable)
                .map(this::mapToDTO);
    }

    private FeedPostDTO mapToDTO(Post post) {
        FeedPostDTO dto = new FeedPostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setAuthorName(post.getUser().getUsername());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setContentPreview(truncateContent(post.getContent()));
        return dto;
    }

    private String truncateContent(String content) {
        if (content == null) return "";
        if (content.length() <= CONTENT_PREVIEW_LENGTH) return content;
        return content.substring(0, CONTENT_PREVIEW_LENGTH).trim() + "...";
    }
}

