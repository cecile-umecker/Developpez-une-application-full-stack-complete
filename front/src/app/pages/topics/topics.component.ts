import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { TopicService } from '../../core/services/topic.service';
import { Topic } from '../../models/topic.model';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-topics',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatCardModule],
  templateUrl: './topics.component.html',
  styleUrls: ['./topics.component.scss']
})
export class TopicsComponent implements OnInit, OnDestroy {
  topics: Topic[] = [];
  private subscriptions = new Subscription();

  constructor(private topicService: TopicService) {}

  ngOnInit(): void {
    const topicSub = this.topicService.topics$.subscribe((topics) => {
      this.topics = topics;
    });
    this.subscriptions.add(topicSub);

    const getAllSub = this.topicService.getAllTopics().subscribe();
    this.subscriptions.add(getAllSub);
  }

  onSubscribe(topicId: number) {
    const sub = this.topicService.subscribeToTopic(topicId).subscribe({
      next: () => console.log('Topic abonné !'),
      error: err => console.error('Erreur subscription', err)
    });
    this.subscriptions.add(sub);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
