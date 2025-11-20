import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subscription, Observable } from 'rxjs';
import { FeedPost } from 'src/app/models/post.model';
import { FeedService } from 'src/app/core/services/feed.service';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';

/**
 * Home page component displaying the post feed.
 * Manages infinite scroll loading, post sorting, and navigation to post creation.
 */
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatIconModule, MatCardModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  /** Observable stream of post feed */
  feed$: Observable<FeedPost[]>;
  /** Loading indicator to prevent multiple simultaneous calls */
  loading = false;
  /** Subscription management to prevent memory leaks */
  private subscriptions = new Subscription();

  constructor(private feedService: FeedService, private router: Router) {
    this.feed$ = this.feedService.feed$;
  }

  /**
   * Initializes the component by resetting the feed and loading the first page.
   */
  ngOnInit() {
    this.feedService.resetFeed();
    this.loadNext();
  }

  /**
   * Loads the next page of posts from the feed.
   * Prevents multiple simultaneous loads using the `loading` flag.
   */
  loadNext() {
    if (this.loading) return;
    this.loading = true;

    const sub = this.feedService.loadNextPage().subscribe(() => {
      this.loading = false;
    });
    this.subscriptions.add(sub);
  }

  /**
   * Detects window scrolling and triggers loading of the next page
   * when the user approaches the bottom of the page (300px from bottom).
   */
  @HostListener('window:scroll', [])
  onWindowScroll() {
    const scrollPosition = window.innerHeight + window.scrollY;
    const threshold = document.body.offsetHeight - 300;
    if (scrollPosition >= threshold) {
      this.loadNext();
    }
  }

  /**
   * Navigates to the new post creation page.
   */
  createPost() {
    this.router.navigate(['/post/create']);
  }

  /**
   * Toggles the sort order (ascending/descending) and reloads the feed.
   */
  toggleSort() {
    this.feedService.toggleSort();
    this.loadNext();
  }

  /**
   * Getter to obtain the current sort state.
   * @returns true if sorting is ascending, false otherwise
   */
  get sortAsc() {
    return this.feedService.sortAsc;
  }

  /**
   * Navigates to the detail page of an post.
   * @param postId - The identifier of the post to display
   */
  viewPost(postId: number) {
    this.router.navigate(['/post', postId]);
  }

  /**
   * Cleans up subscriptions when the component is destroyed.
   */
  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
