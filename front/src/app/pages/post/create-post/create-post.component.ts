import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TopicService } from 'src/app/core/services/topic.service';
import { PostService } from 'src/app/core/services/post.service';
import { NewPost } from 'src/app/models/post.model';
import { Router } from '@angular/router';
import { GoBackComponent } from 'src/app/utils/go-back/go-back.component';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { Subscription } from 'rxjs';

/**
 * New post creation component.
 * Allows users to create an post by selecting a topic and providing a title and content.
 */
@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, GoBackComponent, MatButtonModule, MatInputModule, MatSelectModule, MatFormFieldModule],
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.scss']
})
export class CreatePostComponent implements OnInit, OnDestroy {
  /** Post creation form */
  form!: FormGroup;
  /** List of topics the user is subscribed to */
  topics: { id: number; title: string }[] = [];
  /** Topics loading indicator */
  loadingTopics = true;
  /** Form submission indicator */
  submitting = false;
  /** Subscription management to prevent memory leaks */
  private subscriptions = new Subscription();

  constructor(
    private fb: FormBuilder,
    private topicService: TopicService,
    private postService: PostService,
    private router: Router
  ) {}

  /**
   * Initializes the component by creating the form and loading the user's topics.
   */
  ngOnInit(): void {
    this.form = this.fb.group({
      topicId: ['', Validators.required],
      title: ['', [Validators.required, Validators.minLength(3)]],
      content: ['', [Validators.required, Validators.minLength(5)]]
    });

    const sub = this.topicService.getUserTopics().subscribe({
      next: (topics) => {
        this.topics = topics;
        this.loadingTopics = false;
      },
      error: () => {
        this.loadingTopics = false;
      }
    });
    this.subscriptions.add(sub);
  }

  /**
   * Submits the post creation form.
   * On success, redirects to the new post's detail page.
   */
  submit(): void {
    if (this.form.invalid) return;

    this.submitting = true;

    const payload: NewPost = this.form.value;

    const sub = this.postService.createPost(payload).subscribe({
      next: (createdPost) => {
        this.submitting = false;
        this.router.navigate(['/post', createdPost.id]);
      },
      error: () => {
        this.submitting = false;
      }
    });
    this.subscriptions.add(sub);
  }

  /**
   * Cleans up subscriptions when the component is destroyed.
   */
  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
