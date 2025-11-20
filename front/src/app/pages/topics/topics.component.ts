import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { TopicService } from '../../core/services/topic.service';
import { Topic } from '../../models/topic.model';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';

/**
 * Topics display and management component.
 * Allows users to view all available topics and subscribe to them.
 */
@Component({
  selector: 'app-topics',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatCardModule],
  templateUrl: './topics.component.html',
  styleUrls: ['./topics.component.scss']
})
export class TopicsComponent implements OnInit, OnDestroy {
  /** List of available topics */
  topics: Topic[] = [];
  /** Subscription management to prevent memory leaks */
  private subscriptions = new Subscription();

  constructor(private topicService: TopicService) {}

  /**
   * Initializes the component by loading all available topics.
   */
  ngOnInit(): void {
    const topicSub = this.topicService.topics$.subscribe((topics) => {
      this.topics = topics;
    });
    this.subscriptions.add(topicSub);

    const getAllSub = this.topicService.getAllTopics().subscribe();
    this.subscriptions.add(getAllSub);
  }

  /**
   * Subscribes the user to a topic.
   * @param topicId - The identifier of the topic to subscribe to
   */
  onSubscribe(topicId: number) {
    const sub = this.topicService.subscribeToTopic(topicId).subscribe();
    this.subscriptions.add(sub);
  }

  /**
   * Cleans up subscriptions when the component is destroyed.
   */
  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
