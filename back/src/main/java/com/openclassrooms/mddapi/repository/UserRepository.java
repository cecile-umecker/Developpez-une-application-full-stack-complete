package com.openclassrooms.mddapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.models.User;

/**
 * Repository interface for User entity database operations.
 * 
 * This repository extends JpaRepository to provide standard CRUD operations
 * for User entities, along with custom query methods for finding users by
 * their unique identifiers (email or username).
 * 
 * Custom query methods:
 * - findByEmail: Retrieves a user by their email address
 * - findByUsernameOrEmail: Retrieves a user by either username or email
 * 
 * All methods return Optional to handle cases where users may not be found,
 * promoting safer null handling in the application.
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);
  Optional<User> findByUsernameOrEmail(String username, String email);

  boolean existsByEmail(String email);
}
