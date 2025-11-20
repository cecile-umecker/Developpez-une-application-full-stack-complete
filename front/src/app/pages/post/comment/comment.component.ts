import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostService } from 'src/app/core/services/post.service';
import { DetailedComment, Page } from 'src/app/models/comment.model';
import { MatCardModule } from '@angular/material/card';
import { Subscription } from 'rxjs';

/**
 * Post comments display component.
 * Loads and displays the list of comments associated with an post.
 */
@Component({
  selector: 'app-comment',
  standalone: true,
  imports: [CommonModule, MatCardModule],
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.scss']
})
export class CommentComponent implements OnInit, OnDestroy {
  /** Identifier of the post whose comments are being displayed */
  @Input() postId!: number;
  /** List of post comments */
  comments: DetailedComment[] = [];
  /** Comments loading indicator */
  loadingComments = true;
  /** Subscription management to prevent memory leaks */
  private subscription?: Subscription;

  constructor(private postService: PostService) {}

  /**
   * Initializes the component by loading the post's comments.
   */
  ngOnInit(): void {
    if (!this.postId) return;
    this.loadComments();
  }

  /**
   * Loads the post's comments from the server.
   * Unsubscribes from the previous subscription before creating a new one.
   */
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

  /**
   * Reloads the comments.
   * Public method called by the parent component after creating a new comment.
   */
  refresh(): void {
    this.loadComments();
  }

  /**
   * Cleans up the subscription when the component is destroyed.
   */
  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }
}
