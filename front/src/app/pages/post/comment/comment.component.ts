import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostService } from 'src/app/core/services/post.service';
import { DetailedComment, Page } from 'src/app/models/comment.model';
import { MatCardModule } from '@angular/material/card';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-comment',
  standalone: true,
  imports: [CommonModule, MatCardModule],
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.scss']
})
export class CommentComponent implements OnInit, OnDestroy {

  @Input() postId!: number;
  comments: DetailedComment[] = [];
  loadingComments = true;
  private subscription?: Subscription;

  constructor(private postService: PostService) {}

  ngOnInit(): void {
    if (!this.postId) return;
    this.loadComments();
  }

  loadComments(): void {
    this.loadingComments = true;
    this.subscription?.unsubscribe();
    this.subscription = this.postService.getComments(this.postId).subscribe({
      next: (comments) => {
        this.comments = comments;
        this.loadingComments = false;
      },
      error: () => this.loadingComments = false
    });
  }

  /** Pour que le parent puisse déclencher un refresh après création */
  refresh(): void {
    this.loadComments();
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }
}
