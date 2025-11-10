package com.openclassrooms.mddapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openclassrooms.mddapi.models.Topic;

/**
 * Repository interface for Topic entity database operations.
 * 
 * This repository extends JpaRepository to provide standard CRUD operations
 * for Topic entities. Topics serve as categories for organizing posts and
 * managing user subscriptions.
 * 
 * The repository inherits all standard JpaRepository methods including save,
 * findById, findAll, delete, and count operations. No custom query methods
 * are currently defined, as the standard operations meet the application's needs.
 * 
 * @author Cécile UMECKER
 
 */
public interface TopicRepository extends JpaRepository<Topic, Long> {

}
