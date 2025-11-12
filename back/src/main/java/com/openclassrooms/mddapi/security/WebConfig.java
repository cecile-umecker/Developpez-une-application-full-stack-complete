package com.openclassrooms.mddapi.security;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Web configuration class for the MDD API application.
 * 
 * This configuration class sets up CORS (Cross-Origin Resource Sharing) settings
 * to allow the front-end application, typically running on a different origin,
 * to interact with the back-end API. It defines allowed origins, methods, and
 * credentials support for cross-origin requests.
 * 
 * Key CORS features configured:
 * - Allows requests from "http://localhost:4200" (default Angular dev server)
 * - Permits common HTTP methods: GET, POST, PUT, DELETE, OPTIONS
 * - Enables credentials (cookies, authorization headers) to be included in requests
 * 
 * This configuration is essential for enabling seamless communication between
 * the front-end and back-end during development and testing.
 * 
 * @author Cécile UMECKER
 *
 */

@Configuration
public class WebConfig {

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true);
      }
    };
  }
}
