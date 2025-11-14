import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { NgFor, NgIf } from '@angular/common';
import { TopicService } from 'src/app/core/services/topic.service';
import { PostService } from 'src/app/core/services/post.service';
import { NewPost } from 'src/app/models/post.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [ReactiveFormsModule, NgFor, NgIf],
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.scss']
})
export class CreatePostComponent implements OnInit {

  form!: FormGroup;
  topics: { id: number; title: string }[] = [];
  loadingTopics = true;
  submitting = false;

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

    this.topicService.getUserTopics().subscribe({
      next: (topics) => {
        this.topics = topics;
        this.loadingTopics = false;
      },
      error: () => {
        this.loadingTopics = false;
      }
    });
  }

  submit(): void {
    if (this.form.invalid) return;

    this.submitting = true;

    const payload: NewPost = this.form.value;

    this.postService.createPost(payload).subscribe({
      next: (createdPost) => {
        this.submitting = false;
        this.router.navigate(['/post', createdPost.id]);
      },
      error: (err) => {
        this.submitting = false;
        console.error(err);
      }
    });
  }
}
