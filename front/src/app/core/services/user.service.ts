import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User, UserUpdate } from 'src/app/models/user.model';

/**
 * Service responsible for managing user profile operations.
 * 
 * This service handles all user-related HTTP requests including:
 * - Fetching current user profile information
 * - Updating user profile data (username, email, password)
 * 
 * All requests require authentication and use HTTP-only cookies
 * for session management.
 * 
 * @Injectable Provided at root level for singleton behavior
 */
@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {}

  /**
   * Retrieves the current authenticated user's profile information.
   * 
   * Makes a GET request to fetch user details including username and email.
   * 
   * @returns Observable<User> containing the user's profile data
   */
  getUser(): Observable<User> {
    return this.http.get<User>('/api/user/me', { withCredentials: true });
  }

  /**
   * Updates the current user's profile information.
   * 
   * Sends a PUT request with updated user data. All fields in UserUpdate
   * are optional, allowing partial updates.
   * 
   * @param data - Object containing fields to update (username, email, and/or password)
   * @returns Observable<User> containing the updated user profile
   */
  updateUser(data: UserUpdate): Observable<User> {
    return this.http.put<User>('/api/user/me', data, { withCredentials: true });
  }
}
