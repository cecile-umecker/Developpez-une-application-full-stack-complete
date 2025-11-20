import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, tap } from 'rxjs';
import { Topics, Topic } from 'src/app/models/topic.model';

/**
 * Service responsible for managing topics and user subscriptions.
 * 
 * This service handles:
 * - Fetching all available topics with subscription status
 * - Retrieving user's subscribed topics
 * - Subscribing to topics
 * - Unsubscribing from topics
 * - Managing topic state with reactive updates
 * 
 * The service uses a BehaviorSubject to maintain and broadcast the current
 * list of topics, allowing components to reactively update when topic data
 * or subscription status changes.
 * 
 * @Injectable Provided at root level for singleton behavior
 */
@Injectable({
  providedIn: 'root'
})
export class TopicService {
  /** BehaviorSubject holding the current list of topics */
  private topicsSubject = new BehaviorSubject<Topics>([]);
  /** Observable stream of topics for component subscriptions */
  topics$ = this.topicsSubject.asObservable();

  constructor(private http: HttpClient) {}

  /**
   * Retrieves all available topics with subscription status.
   * 
   * Fetches the complete list of topics from the API, with each topic
   * indicating whether the current user is subscribed to it.
   * Updates the topics$ observable with the retrieved data.
   * 
   * @returns Observable<Topics> containing all topics with subscription flags
   */
  getAllTopics() {
    return this.http.get<Topics>('/api/topic', { withCredentials: true }).pipe(
      tap(data => this.topicsSubject.next(data))
    );
  }

  /**
   * Retrieves only the topics the current user is subscribed to.
   * 
   * Fetches topics that the user has an active subscription to.
   * These topics determine which posts appear in the user's feed.
   * Updates the topics$ observable with the retrieved data.
   * 
   * @returns Observable<Topics> containing user's subscribed topics
   */
  getUserTopics() {
    return this.http.get<Topics>('/api/topic/my', { withCredentials: true }).pipe(
      tap(data => this.topicsSubject.next(data))
    );
  }

  /**
   * Subscribes the current user to a specific topic.
   * 
   * Creates a subscription relationship between the user and the topic.
   * After subscribing, posts from this topic will appear in the user's feed.
   * Updates the local topics list with the new subscription status.
   * 
   * @param topicId - The unique identifier of the topic to subscribe to
   * @returns Observable<Topic> containing the updated topic with subscription status
   */
  subscribeToTopic(topicId: number) {
    return this.http.post<Topic>(`/api/topic/${topicId}/subscription`, {}, { withCredentials: true }).pipe(
      tap(updatedTopic => this.updateTopicInList(updatedTopic))
    );
  }

  /**
   * Unsubscribes the current user from a specific topic.
   * 
   * Removes the subscription relationship between the user and the topic.
   * After unsubscribing, posts from this topic will no longer appear in the user's feed.
   * Updates the local topics list with the new subscription status.
   * 
   * @param topicId - The unique identifier of the topic to unsubscribe from
   * @returns Observable<Topic> containing the updated topic with subscription status
   */
  unsubscribeFromTopic(topicId: number) {
    return this.http.delete<Topic>(`/api/topic/${topicId}/subscription`, { withCredentials: true }).pipe(
      tap(updatedTopic => this.updateTopicInList(updatedTopic))
    );
  }

  /**
   * Updates a specific topic in the local topics list.
   * 
   * This private method updates the BehaviorSubject with the modified topic,
   * ensuring that all subscribers receive the updated topic data (particularly
   * the subscription status change).
   * 
   * @param updatedTopic - The topic with updated information to replace in the list
   */
  private updateTopicInList(updatedTopic: Topic) {
    const current = this.topicsSubject.getValue();
    if (!current) return;

    const updatedList = current.map(topic =>
      topic.id === updatedTopic.id ? updatedTopic : topic
    );

    this.topicsSubject.next(updatedList);
  }
}
