import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from 'src/app/models/user.model';
import { AuthResponse, LoginCredentials, RegisterData } from 'src/app/models/auth.model';
import { BehaviorSubject, catchError, map, Observable, of, tap } from 'rxjs';

/**
 * Service responsible for managing user authentication and session state.
 * 
 * This service handles all authentication-related operations including:
 * - User login and registration
 * - Session management with local storage
 * - Authentication state broadcasting via observables
 * - Server-side authentication validation
 * - User logout
 * 
 * The service maintains authentication state both in memory and in local storage
 * for persistence across page refreshes. It uses a BehaviorSubject to notify
 * components of authentication state changes.
 * 
 * @Injectable Provided at root level for singleton behavior
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  /** Internal flag tracking logged-in state in memory */
  private loggedIn = false;
  /** Currently authenticated user information */
  private currentUser?: User;
  /** BehaviorSubject for broadcasting authentication state changes */
  private authChanged = new BehaviorSubject<boolean>(this.isLoggedIn());
  /** Observable stream of authentication state changes */
  authChanged$ = this.authChanged.asObservable();

  constructor(private http: HttpClient) { }

  /**
   * Authenticates a user with their credentials.
   * 
   * Makes a POST request to the login endpoint with credentials.
   * Upon successful login:
   * - Sets the logged-in state to true
   * - Stores authentication flag in local storage
   * - Broadcasts authentication state change
   * 
   * @param credentials - User's login credentials (username/email and password)
   * @returns Observable<AuthResponse> with the authentication result message
   */
  login(credentials: LoginCredentials): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('/api/auth/login', credentials, { withCredentials: true }).pipe(
      tap(() => {
        this.loggedIn = true;
        localStorage.setItem('loggedIn', 'true');
        this.authChanged.next(true);
      })
    );
  }

  /**
   * Registers a new user account.
   * 
   * Makes a POST request to the registration endpoint.
   * Note: This does not automatically log in the user after registration.
   * 
   * @param data - User registration data (username, email, password)
   * @returns Observable<AuthResponse> with the registration result message
   */
  register(data: RegisterData): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('/api/auth/register', data, { withCredentials: true });
  }

  /**
   * Logs out the current user and clears session data.
   * 
   * Makes a POST request to the logout endpoint.
   * Upon successful logout:
   * - Removes authentication flag from local storage
   * - Clears logged-in state and current user
   * - Broadcasts authentication state change
   * 
   * @returns Observable<AuthResponse> with the logout result message
   */
  logout(): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('/api/auth/logout', {}, { withCredentials: true }).pipe(
      tap(() => {
        localStorage.removeItem('loggedIn');
        this.loggedIn = false;
        this.currentUser = undefined;
        this.authChanged.next(false);
      })
    );
  }

  /**
   * Validates user authentication with the server.
   * 
   * Makes a GET request to fetch the current user's profile from the server.
   * This performs server-side validation of the authentication session.
   * 
   * On success:
   * - Updates local storage with authentication flag
   * - Sets logged-in state and stores user data
   * - Broadcasts authentication state change
   * - Returns true
   * 
   * On failure:
   * - Clears local storage authentication flag
   * - Resets logged-in state
   * - Broadcasts authentication state change
   * - Returns false
   * 
   * @returns Observable<boolean> - true if authenticated, false otherwise
   */
  isAuthenticated(): Observable<boolean> {
    return this.http.get<User>('/api/user/me', { withCredentials: true }).pipe(
      tap(user => {
        localStorage.setItem('loggedIn', 'true');
        this.loggedIn = true;
        this.currentUser = user;
        this.authChanged.next(true);
      }),
      map(() => true),
      catchError(() => {
        localStorage.removeItem('loggedIn');
        this.loggedIn = false;
        this.authChanged.next(false);
        return of(false);
      })
    );
  }

  /**
   * Checks if user is logged in using local state.
   * 
   * This is a synchronous check that verifies authentication status from:
   * 1. In-memory flag (loggedIn)
   * 2. Local storage flag ('loggedIn')
   * 
   * Note: This does not validate with the server and may not reflect
   * the actual server-side session state. Use isAuthenticated() for
   * server validation.
   * 
   * @returns boolean - true if logged in locally, false otherwise
   */
  isLoggedIn(): boolean {
    return this.loggedIn || localStorage.getItem('loggedIn') === 'true';
  }
}
