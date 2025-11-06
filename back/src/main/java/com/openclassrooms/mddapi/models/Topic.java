package com.openclassrooms.mddapi.models;

import java.util.*;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a topic in the MDD API application.
 * 
 * A topic serves as a category or subject area for organizing posts.
 * Users can create posts within topics to facilitate organized discussions
 * and content sharing around specific themes or subjects.
 * 
 * Each topic has a unique title and can contain multiple posts. The topic
 * maintains a one-to-many relationship with posts, where deleting a topic
 * will cascade delete all associated posts.
 * 
 * This entity uses JPA annotations for persistence and Lombok annotations
 * for reducing boilerplate code (getters, setters, constructors, builder pattern).
 */

@Entity
@Table(name = "topic")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topic {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String title;

  @Column(nullable = false)
  private String description;

  @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Post> posts = new ArrayList<>();
}
