import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PostService } from 'src/app/core/services/post.service';
import { DetailedPost } from 'src/app/models/post.model';
import { CommentComponent } from './comment/comment.component';
import { CreateCommentComponent } from './comment/create-comment/create-comment.component';
import { GoBackComponent } from 'src/app/utils/go-back/go-back.component';
import { MatDividerModule } from '@angular/material/divider';
import { Subscription } from 'rxjs';

/**
 * Post detail display component.
 * Displays the complete content of an post along with its comments.
 */
@Component({
  selector: 'app-post',
  standalone: true,
  imports: [CommonModule, CommentComponent, CreateCommentComponent, GoBackComponent, MatDividerModule],
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent implements OnInit, OnDestroy {
  /** Identifier of the post to display */
  postId!: number;
  /** Detailed post data */
  post?: DetailedPost;
  /** post loading indicator */
  loadingPost = true;
  /** Subscription management to prevent memory leaks */
  private subscription?: Subscription;

  constructor(
    private route: ActivatedRoute,
    private postService: PostService
  ) {}

  /**
   * Initializes the component by retrieving the post ID from the URL and loading its data.
   */
  ngOnInit(): void {
    this.postId = Number(this.route.snapshot.paramMap.get('id'));
    if (isNaN(this.postId)) return;

    this.loadPost();
  }

  /**
   * Loads the detailed post data from the server.
   * @private
   */
  private loadPost(): void {
    this.loadingPost = true;
    this.subscription = this.postService.getPost(this.postId).subscribe({
      next: (post) => {
        this.post = post;
        this.loadingPost = false;
      },
      error: () => {
        this.loadingPost = false;
      }
    });
  }

  /**
   * Cleans up the subscription when the component is destroyed.
   */
  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }
}
