import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subscription, Observable } from 'rxjs';
import { FeedPost } from 'src/app/models/post.model';
import { FeedService } from 'src/app/core/services/feed.service';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatIconModule, MatCardModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  feed$: Observable<FeedPost[]>;
  loading = false;
  private subscriptions = new Subscription();

  constructor(private feedService: FeedService, private router: Router) {
    this.feed$ = this.feedService.feed$;
  }

  ngOnInit() {
    this.feedService.resetFeed();
    this.loadNext();
  }

  loadNext() {
    if (this.loading) return;
    this.loading = true;

    const sub = this.feedService.loadNextPage().subscribe(() => {
      this.loading = false;
    });
    this.subscriptions.add(sub);
  }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    const scrollPosition = window.innerHeight + window.scrollY;
    const threshold = document.body.offsetHeight - 300;
    if (scrollPosition >= threshold) {
      this.loadNext();
    }
  }

  createPost() {
    this.router.navigate(['/post/create']);
  }

  toggleSort() {
    this.feedService.toggleSort();
    this.loadNext();
  }

  get sortAsc() {
    return this.feedService.sortAsc;
  }

  viewPost(postId: number) {
    this.router.navigate(['/post', postId]);
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe(); // annule toutes les subscriptions
  }
}
