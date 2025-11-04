package com.openclassrooms.mddapi.models;

import java.util.*;

import jakarta.persistence.*;
import lombok.*;

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
