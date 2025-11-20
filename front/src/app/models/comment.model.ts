/**
 * Represents the data structure for creating a new comment.
 * Used when submitting a comment to a post.
 */
export interface NewComment {
  /** Content of the comment */
  content: string;
}

/**
 * Generic paginated response structure from Spring Data.
 * Can be used for any paginated content type.
 * 
 * @template T - The type of content items in the page
 */
export interface Page<T> {
  /** Array of content items in the current page */
  content: T[];
  /** Pagination metadata */
  pageable: any;
  /** Whether this is the last page */
  last: boolean;
  /** Total number of pages available */
  totalPages: number;
  /** Total number of items across all pages */
  totalElements: number;
  /** Number of items per page */
  size: number;
  /** Current page number (zero-based) */
  number: number;
  /** Sorting information */
  sort: any;
  /** Whether this is the first page */
  first: boolean;
  /** Number of items in the current page */
  numberOfElements: number;
  /** Whether the page is empty */
  empty: boolean;
}

/**
 * Represents a comment with full details.
 * Used when displaying comments under a post.
 */
export interface DetailedComment {
  /** Unique identifier of the comment */
  id: number;
  /** Username of the comment author */
  authorName: string;
  /** Content of the comment */
  content: string;
  /** Date and time when the comment was created */
  createdAt: Date;
}