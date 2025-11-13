import { Component, OnInit, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import { FeedPost } from 'src/app/models/post.model';
import { FeedService } from 'src/app/core/services/feed.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  feed$: Observable<FeedPost[]>;
  loading = false;

  constructor(private feedService: FeedService, private router: Router) {
    this.feed$ = this.feedService.feed$;
  }

  ngOnInit() {
    this.loadNext();
  }

  loadNext() {
    if (this.loading) return;
    this.loading = true;
    this.feedService.loadNextPage().subscribe(() => {
      this.loading = false;
    });
  }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    const scrollPosition = window.innerHeight + window.scrollY;
    const threshold = document.body.offsetHeight - 300; // déclenche 300px avant le bas
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
}
