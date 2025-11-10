package com.openclassrooms.mddapi.models;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a user in the MDD API application.
 * 
 * A user is the central entity that can create posts and comments, as well as
 * subscribe to topics of interest. Each user has unique credentials (username
 * and email) for authentication and identification purposes.
 * 
 * The user entity maintains several relationships:
 * - Many-to-many relationship with topics (subscriptions)
 * - One-to-many relationship with posts (authored content)
 * - One-to-many relationship with comments (user feedback)
 * 
 * Creation and update timestamps are automatically managed through JPA lifecycle
 * callbacks. The password field should store encrypted passwords for security.
 * 
 * This entity uses JPA annotations for persistence and Lombok annotations
 * for reducing boilerplate code (getters, setters, constructors, builder pattern).
 * 
 * @author Cécile UMECKER
 
 */

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;  

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  /**
   * List of topics the user is subscribed to.
   * Represents a many-to-many relationship with the Topic entity.
   */
  @ManyToMany
  @JoinTable(name = "user_topic", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "topic_id"))
  @Builder.Default
  private List<Topic> topics = new ArrayList<>();

  /**
   * List of posts created by this user.
   * Represents a one-to-many relationship with the Post entity.
   */
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Post> posts = new ArrayList<>();

  /**
   * List of comments created by this user.
   * Represents a one-to-many relationship with the Comment entity.
   */
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Comment> comments = new ArrayList<>();

  /**
   * Lifecycle callback method executed before persisting a new user entity.
   * Automatically sets the creation and update timestamps to the current date and time.
   */
  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  } 

  /**
   * Lifecycle callback method executed before updating an existing user entity.
   * Automatically updates the update timestamp to the current date and time.
   */
  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  } 
}
