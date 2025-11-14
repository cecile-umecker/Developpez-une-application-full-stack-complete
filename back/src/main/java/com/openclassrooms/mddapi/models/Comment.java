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
 * 
 * @author Cécile UMECKER
 
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

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  /**
   * The post this comment belongs to.
   * Represents a many-to-one relationship with the Post entity.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  /**
   * The user who authored this comment.
   * Represents a many-to-one relationship with the User entity.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  /**
   * JPA lifecycle callback that automatically sets the creation timestamp.
   * This method is invoked before the entity is persisted to the database.
   */
  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  } 
}
