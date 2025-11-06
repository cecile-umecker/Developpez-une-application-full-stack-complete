package com.openclassrooms.mddapi.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a comment in the MDD API application.
 * 
 * A comment is associated with a post and a user, allowing users to provide
 * feedback and engage in discussions on posts. Each comment tracks its creation
 * time automatically.
 * 
 * This entity uses JPA annotations for persistence and Lombok annotations
 * for reducing boilerplate code (getters, setters, constructors, builder pattern).
 */

@Entity
@Table(name = "comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  } 
}
