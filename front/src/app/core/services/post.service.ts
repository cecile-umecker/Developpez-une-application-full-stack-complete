import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DetailedPost, NewPost } from 'src/app/models/post.model';
import { DetailedComment, NewComment, Page } from 'src/app/models/comment.model';

/**
 * Service responsible for managing posts and comments.
 * 
 * This service handles all post-related HTTP operations including:
 * - Fetching individual post details
 * - Creating new posts within topics
 * - Retrieving comments for posts
 * - Adding new comments to posts
 * 
 * All requests require authentication and use HTTP-only cookies
 * for session management.
 * 
 * @Injectable Provided at root level for singleton behavior
 */
@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private http: HttpClient) {}

  /**
   * Retrieves detailed information for a specific post.
   * 
   * Fetches complete post data including title, content, author,
   * topic, and creation date.
   * 
   * @param id - The unique identifier of the post to retrieve
   * @returns Observable<DetailedPost> containing the complete post information
   */
  getPost(id: number): Observable<DetailedPost> {
    return this.http.get<DetailedPost>(`/api/post/${id}`, {
      withCredentials: true
    });
  }

  /**
   * Creates a new post within a specific topic.
   * 
   * Submits post data (title, content, topic ID) to create a new post.
   * The post is automatically associated with the authenticated user as author.
   * 
   * @param data - New post data containing title, content, and topic ID
   * @returns Observable<DetailedPost> containing the created post with full details
   */
  createPost(data: NewPost): Observable<DetailedPost> {
    return this.http.post<DetailedPost>('/api/post', data, {
      withCredentials: true
    });
  }

  /**
   * Retrieves all comments for a specific post.
   * 
   * Fetches a list of comments associated with the post, ordered by
   * creation date in ascending order.
   * 
   * @param postId - The unique identifier of the post
   * @returns Observable<DetailedComment[]> containing all comments for the post
   */
  getComments(postId: number): Observable<DetailedComment[]> {
    return this.http.get<DetailedComment[]>(`/api/post/${postId}/comments`, {
      withCredentials: true
    });
  }

  /**
   * Adds a new comment to a specific post.
   * 
   * Submits comment content to create a new comment on the post.
   * The comment is automatically associated with the authenticated user as author.
   * 
   * @param postId - The unique identifier of the post to comment on
   * @param data - New comment data containing the comment content
   * @returns Observable<DetailedComment> containing the created comment with full details
   */
  createComment(postId: number, data: NewComment): Observable<DetailedComment> {
    return this.http.post<DetailedComment>(`/api/post/${postId}/comments`, data, {
      withCredentials: true
    });
  }
}
