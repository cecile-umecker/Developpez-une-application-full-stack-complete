/**
 * Represents the credentials required for user login.
 * The login field accepts either username or email.
 */
export interface LoginCredentials {
  /** Username or email address */
  login: string;
  /** User's password */
  password: string;
}

/**
 * Represents the data required for user registration.
 * All fields are mandatory for creating a new account.
 */
export interface RegisterData {
  /** Desired username for the new account */
  username: string;
  /** Email address for the new account */
  email: string;
  /** Password for the new account */
  password: string;
}

/**
 * Represents the response from authentication endpoints.
 * Contains a message indicating the result of the operation.
 */
export interface AuthResponse {
  /** Message describing the authentication result (success or error) */
  message: string;
}
