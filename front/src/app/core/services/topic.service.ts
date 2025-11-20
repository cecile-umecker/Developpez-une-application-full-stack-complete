import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, tap } from 'rxjs';
import { Topics, Topic } from 'src/app/models/topic.model';

@Injectable({
  providedIn: 'root'
})
export class TopicService {
  private topicsSubject = new BehaviorSubject<Topics>([]);
  topics$ = this.topicsSubject.asObservable();

  constructor(private http: HttpClient) {}

  getAllTopics() {
    return this.http.get<Topics>('/api/topic', { withCredentials: true }).pipe(
      tap(data => this.topicsSubject.next(data))
    );
  }

  getUserTopics() {
    return this.http.get<Topics>('/api/topic/my', { withCredentials: true }).pipe(
      tap(data => this.topicsSubject.next(data))
    );
  }

  subscribeToTopic(topicId: number) {
    return this.http.post<Topic>(`/api/topic/${topicId}/subscription`, {}, { withCredentials: true }).pipe(
      tap(updatedTopic => this.updateTopicInList(updatedTopic))
    );
  }

  unsubscribeFromTopic(topicId: number) {
    return this.http.delete<Topic>(`/api/topic/${topicId}/subscription`, { withCredentials: true }).pipe(
      tap(updatedTopic => this.updateTopicInList(updatedTopic))
    );
  }

  private updateTopicInList(updatedTopic: Topic) {
    const current = this.topicsSubject.getValue();
    if (!current) return;

    const updatedList = current.map(topic =>
      topic.id === updatedTopic.id ? updatedTopic : topic
    );

    this.topicsSubject.next(updatedList);
  }
}
