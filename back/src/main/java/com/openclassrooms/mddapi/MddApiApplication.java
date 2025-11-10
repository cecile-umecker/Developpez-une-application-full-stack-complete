package com.openclassrooms.mddapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Main application class for the MDD (Monde de Dev) API.
 * 
 * This is the entry point for the Spring Boot application, responsible for
 * bootstrapping and launching the entire MDD API backend server. The application
 * provides RESTful APIs for managing topics, posts, comments, and user authentication.
 * 
 * On startup, this class loads environment variables from a .env file and sets them
 * as system properties for use throughout the application. This includes:
 * - Database connection settings (URL, username, password)
 * - JWT authentication configuration (secret key, token expiration)
 * 
 * The @SpringBootApplication annotation enables auto-configuration, component scanning,
 * and configuration properties, making this a complete Spring Boot application ready
 * to serve HTTP requests.
 * 
 * @author Cécile UMECKER
 
 */

@SpringBootApplication
public class MddApiApplication {

	/**
	 * Main entry point for the MDD API application.
	 * 
	 * This method performs the following initialization steps:
	 * 1. Loads environment variables from the .env file using Dotenv library
	 * 2. Sets system properties for database configuration (URL, user, password)
	 * 3. Sets system properties for JWT configuration (secret key, expiration time)
	 * 4. Launches the Spring Boot application context
	 * 
	 * Environment variables are converted to system properties to make them accessible
	 * throughout the application via @Value annotations or System.getProperty() calls.
	 * 
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
			System.setProperty("DB_URL", dotenv.get("DB_URL"));
			System.setProperty("DB_USER", dotenv.get("DB_USER"));
			System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
			System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
			System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));		
		
		SpringApplication.run(MddApiApplication.class, args);
	}

}
