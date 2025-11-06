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
 */

@SpringBootApplication
public class MddApiApplication {

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
