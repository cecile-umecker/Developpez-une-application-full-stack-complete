import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PostService } from 'src/app/core/services/post.service';
import { DetailedPost } from 'src/app/models/post.model';
import { CommentComponent } from './comment/comment.component';
import { CreateCommentComponent } from './comment/create-comment/create-comment.component';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [CommonModule, CommentComponent, CreateCommentComponent],
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent implements OnInit {

  postId!: number;
  post?: DetailedPost;
  loadingPost = true;

  constructor(
    private route: ActivatedRoute,
    private postService: PostService
  ) {}

  ngOnInit(): void {
    this.postId = Number(this.route.snapshot.paramMap.get('id'));
    if (isNaN(this.postId)) return;

    this.loadPost();
  }

  private loadPost(): void {
    this.loadingPost = true;
    this.postService.getPost(this.postId).subscribe({
      next: (post) => {
        this.post = post;
        this.loadingPost = false;
      },
      error: () => {
        this.loadingPost = false;
      }
    });
  }
}
