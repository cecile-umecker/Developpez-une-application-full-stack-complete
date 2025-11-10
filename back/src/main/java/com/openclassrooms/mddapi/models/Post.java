package com.openclassrooms.mddapi.models;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a post in the MDD API application.
 * 
 * A post is a piece of content created by a user within a specific topic.
 * Users can write posts to share information, start discussions, or contribute
 * to a topic. Each post can have multiple comments associated with it.
 * 
 * The post maintains bidirectional relationships with users (author), topics
 * (category), and comments (feedback). The creation timestamp is automatically
 * set when the post is persisted to the database.
 * 
 * This entity uses JPA annotations for persistence and Lombok annotations
 * for reducing boilerplate code (getters, setters, constructors, builder pattern).
 * 
 * @author Cécile UMECKER
 
 */

@Entity
@Table(name = "post")
@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  /**
   * The user who authored this post.
   * Represents a many-to-one relationship with the User entity.
   */
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  /**
   * The topic this post belongs to.
   * Represents a many-to-one relationship with the Topic entity.
   */
  @ManyToOne
  @JoinColumn(name = "topic_id", nullable = false)
  private Topic topic;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  /**
   * List of comments associated with this post.
   * Represents a one-to-many relationship with the Comment entity.
   * Deleting a post will cascade delete all associated comments.
   */
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Comment> comments = new ArrayList<>();

  /**
   * JPA lifecycle callback that automatically sets the creation timestamp.
   * This method is invoked before the entity is persisted to the database.
   */
  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  } 
}
