/**
 * Represents a user in the MDD application.
 * Contains the basic user information displayed in the profile.
 */
export interface User {
  /** The unique username of the user */
  username: string;
  /** The email address of the user */
  email: string;
}

/**
 * Represents the data structure for updating user profile information.
 * All fields are optional to allow partial updates.
 */
export interface UserUpdate {
  /** New username (optional) */
  username?: string;
  /** New email address (optional) */
  email?: string;
  /** New password (optional) */
  password?: string;
}