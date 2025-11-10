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
 * their unique identifiers (email or username) and checking email existence.
 * 
 * Custom query methods allow flexible user lookup during authentication and
 * validation processes. All Optional-returning methods promote safer null
 * handling in the application.
 * 
 * @author Cécile UMECKER
 
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Finds a user by their email address.
   * 
   * This method performs a case-sensitive lookup for a user with the exact
   * email address provided. It is commonly used during authentication and
   * user validation processes.
   * 
   * @param email the email address to search for
   * @return Optional containing the User if found, empty Optional otherwise
   */
  Optional<User> findByEmail(String email);

  /**
   * Finds a user by either their username or email address.
   * 
   * This method provides flexible user lookup, checking both username and email
   * fields. It is particularly useful during login where users can authenticate
   * with either their username or email. The search is case-sensitive.
   * 
   * @param username the username to search for
   * @param email the email address to search for
   * @return Optional containing the User if found by either username or email, empty Optional otherwise
   */
  Optional<User> findByUsernameOrEmail(String username, String email);

  /**
   * Checks if a user with the specified email address exists.
   * 
   * This method performs an existence check without retrieving the full user entity,
   * making it more efficient than findByEmail when only validation is needed.
   * It is commonly used during registration and profile updates to ensure email uniqueness.
   * 
   * @param email the email address to check for existence
   * @return true if a user with this email exists, false otherwise
   */
  boolean existsByEmail(String email);
}
