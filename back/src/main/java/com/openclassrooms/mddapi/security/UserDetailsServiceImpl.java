package com.openclassrooms.mddapi.security;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Custom implementation of Spring Security's UserDetailsService interface.
 * 
 * This service provides user authentication details to Spring Security by loading
 * user information from the database. It serves as a bridge between the application's
 * User entity and Spring Security's UserDetails interface.
 * 
 * The implementation supports flexible user lookup by allowing authentication with
 * either username or email address. When a user is found, their credentials are
 * converted to Spring Security's UserDetails format for authentication processing.
 * 
 * This class is essential for Spring Security's authentication mechanism, as it
 * retrieves user credentials and authorities during the login process.
 * 
 * @author Cécile UMECKER
 
 */

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads user details by username or email for authentication.
     * 
     * This method is called by Spring Security during authentication to retrieve
     * user credentials. It searches for a user by either username or email (the
     * login parameter can be either), making authentication more flexible.
     * 
     * The method converts the application's User entity to Spring Security's
     * UserDetails format, containing email (used as username), password, and
     * authorities (currently empty, as role-based authorization is not implemented).
     * 
     * @param login the username or email to search for
     * @return UserDetails containing user credentials and authorities
     * @throws UsernameNotFoundException if no user is found with the given username or email
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(login, login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), 
                user.getPassword(), 
                new ArrayList<>()
        );
    }
}
