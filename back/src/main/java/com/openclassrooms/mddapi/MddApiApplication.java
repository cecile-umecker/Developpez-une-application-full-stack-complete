package com.openclassrooms.mddapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class MddApiApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
			System.setProperty("SPRING_USER_NAME", dotenv.get("SPRING_USER_NAME"));
			System.setProperty("SPRING_USER_PASSWORD", dotenv.get("SPRING_USER_PASSWORD"));
			System.setProperty("DB_URL", dotenv.get("DB_URL"));
			System.setProperty("DB_USER", dotenv.get("DB_USER"));
			System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
			System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
			System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));		
		
		SpringApplication.run(MddApiApplication.class, args);
	}

}
