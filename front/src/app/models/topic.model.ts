/**
 * Represents a discussion topic in the MDD application.
 * Topics are used to categorize posts and manage user subscriptions.
 */
export interface Topic {
  /** Unique identifier of the topic */
  id: number;
  /** Title of the topic */
  title: string;
  /** Description explaining the topic's purpose */
  description: string;
  /** Flag indicating if the current user is subscribed to this topic */
  subscribed: boolean;
}

/**
 * Type alias for an array of topics.
 * Used for representing collections of topics in the application.
 */
export type Topics = Topic[];

