import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable, map, tap } from 'rxjs';
import { FeedPost, Feed } from 'src/app/models/post.model';

/**
 * Service responsible for managing the user's personalized feed.
 * 
 * This service handles:
 * - Infinite scroll pagination for feed posts
 * - Loading posts from subscribed topics
 * - Sorting posts by creation date (ascending or descending)
 * - Feed state management with reactive updates
 * 
 * The service maintains feed state using a BehaviorSubject, accumulating
 * posts as pages are loaded. It tracks pagination state to prevent
 * duplicate requests and handles sort order toggling with feed reset.
 * 
 * @Injectable Provided at root level for singleton behavior
 */
@Injectable({
  providedIn: 'root'
})
export class FeedService {
  /** BehaviorSubject holding the accumulated list of feed posts */
  private feedSubject = new BehaviorSubject<FeedPost[]>([]);
  /** Observable stream of feed posts for component subscriptions */
  feed$ = this.feedSubject.asObservable();

  /** Current page number for pagination (zero-based) */
  private currentPage = 0;
  /** Number of posts to load per page */
  private pageSize = 10;
  /** Flag indicating if the last page has been reached */
  private lastPage = false;
  /** Sort order flag: false for descending (newest first), true for ascending */
  sortAsc = false;

  constructor(private http: HttpClient) {}

  /**
   * Loads the next page of feed posts.
   * 
   * Fetches posts from subscribed topics with pagination and sorting.
   * Appends new posts to the existing feed without replacing previous pages.
   * 
   * If the last page has already been loaded, returns the current feed
   * observable without making a new request.
   * 
   * Updates pagination state and appends posts to the feed$ observable.
   * 
   * @returns Observable<FeedPost[]> containing the newly loaded posts
   */
  loadNextPage(): Observable<FeedPost[]> {
    if (this.lastPage) return this.feed$;

    const sortParam = this.sortAsc ? 'createdAt,asc' : 'createdAt,desc';

    const params = new HttpParams()
      .set('page', this.currentPage.toString())
      .set('size', this.pageSize.toString())
      .set('sort', sortParam);

    return this.http.get<Feed>('/api/feed', { params, withCredentials: true }).pipe(
      tap(resp => {
        this.currentPage = resp.number + 1;
        this.lastPage = resp.last;
        this.feedSubject.next([...this.feedSubject.value, ...resp.content]);
      }),
      map(resp => resp.content)
    );
  }

  /**
   * Resets the feed to its initial state.
   * 
   * Clears all loaded posts and resets pagination state.
   * Use this method when you need to reload the feed from scratch,
   * such as after changing sort order or refreshing content.
   */
  resetFeed() {
    this.currentPage = 0;
    this.lastPage = false;
    this.feedSubject.next([]);
  }

  /**
   * Toggles the sort order and resets the feed.
   * 
   * Switches between ascending and descending sort order by creation date,
   * then resets the feed to reload posts with the new sort order.
   */
  toggleSort() {
    this.sortAsc = !this.sortAsc;
    this.resetFeed();
  }
}
