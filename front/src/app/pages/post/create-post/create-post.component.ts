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

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, GoBackComponent, MatButtonModule, MatInputModule, MatSelectModule, MatFormFieldModule],
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.scss']
})
export class CreatePostComponent implements OnInit, OnDestroy {

  form!: FormGroup;
  topics: { id: number; title: string }[] = [];
  loadingTopics = true;
  submitting = false;
  private subscriptions = new Subscription();

  constructor(
    private fb: FormBuilder,
    private topicService: TopicService,
    private postService: PostService,
    private router: Router
  ) {}

  ngOnInit(): void {
    console.log('createpost component loaded');
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

  submit(): void {
    if (this.form.invalid) return;

    this.submitting = true;

    const payload: NewPost = this.form.value;

    const sub = this.postService.createPost(payload).subscribe({
      next: (createdPost) => {
        this.submitting = false;
        this.router.navigate(['/post', createdPost.id]);
      },
      error: (err) => {
        this.submitting = false;
        console.error(err);
      }
    });
    this.subscriptions.add(sub);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
