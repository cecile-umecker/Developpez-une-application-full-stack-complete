/**
 * Represents a post in the user's feed with preview information.
 * Used for displaying posts in the home feed with truncated content.
 */
export interface FeedPost {
  /** Unique identifier of the post */
  id: number;
  /** Title of the post */
  title: string;
  /** Username of the post author */
  authorName: string;
  /** Truncated preview of the post content (first 150 characters) */
  contentPreview: string;
  /** ISO timestamp of when the post was created */
  createdAt: string;
}

/**
 * Represents pagination metadata from Spring Data.
 * Contains information about the current page position and sorting.
 */
export interface Pageable {
  /** Current page number (zero-based) */
  pageNumber: number;
  /** Number of items per page */
  pageSize: number;
  /** Sorting information for the page */
  sort: {
    /** Whether the sort is empty */
    empty: boolean;
    /** Whether the results are sorted */
    sorted: boolean;
    /** Whether the results are unsorted */
    unsorted: boolean;
  };
  /** Offset from the start of the dataset */
  offset: number;
  /** Whether pagination is enabled */
  paged: boolean;
  /** Whether pagination is disabled */
  unpaged: boolean;
}

/**
 * Represents a paginated feed response from the API.
 * Contains an array of posts along with pagination metadata.
 */
export interface Feed {
  /** Array of feed posts in the current page */
  content: FeedPost[];
  /** Pagination metadata */
  pageable: Pageable;
  /** Whether this is the last page */
  last: boolean;
  /** Total number of posts across all pages */
  totalElements: number;
  /** Total number of pages available */
  totalPages: number;
  /** Number of items in the current page */
  size: number;
  /** Current page number (zero-based) */
  number: number;
  /** Sorting information for the feed */
  sort: {
    /** Whether the sort is empty */
    empty: boolean;
    /** Whether the results are sorted */
    sorted: boolean;
    /** Whether the results are unsorted */
    unsorted: boolean;
  };
  /** Whether this is the first page */
  first: boolean;
  /** Number of elements in the current page */
  numberOfElements: number;
  /** Whether the feed is empty */
  empty: boolean;
}

/**
 * Represents the complete details of a single post.
 * Used when displaying the full post content and metadata.
 */
export interface DetailedPost {
  /** Unique identifier of the post */
  id: number;
  /** Title of the post */
  title: string;
  /** Full content of the post */
  content: string;
  /** Username of the post author */
  authorName: string;
  /** Title of the topic this post belongs to */
  topicTitle: string;
  /** Date and time when the post was created */
  createdAt: Date;
}

/**
 * Represents the data structure for creating a new post.
 * Used when submitting a new post to the API.
 */
export interface NewPost {
  /** Title of the new post */
  title: number;
  /** Content of the new post */
  content: string;
  /** ID of the topic this post belongs to */
  topicId: number;
}