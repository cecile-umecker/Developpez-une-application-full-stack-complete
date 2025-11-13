import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { TopicService } from '../../core/services/topic.service';
import { Topic } from '../../models/topic.model';

@Component({
  selector: 'app-topics',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './topics.component.html',
  styleUrls: ['./topics.component.scss']
})
export class TopicsComponent implements OnInit, OnDestroy {
  topics: Topic[] = [];
  private topicSub?: Subscription;

  constructor(private topicService: TopicService) {}

  ngOnInit(): void {
    this.topicSub = this.topicService.topics$.subscribe((topics) => {
      this.topics = topics;
    });

    this.topicService.getAllTopics().subscribe();

  }

  onSubscribe(topicId: number) {
    this.topicService.subscribeToTopic(topicId).subscribe({
      next: () => console.log('Topic abonné !'),
      error: err => console.error('Erreur subscription', err)
    });
  }


  ngOnDestroy(): void {
    this.topicSub?.unsubscribe();
  }
}
