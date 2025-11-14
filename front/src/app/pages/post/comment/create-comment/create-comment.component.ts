import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PostService } from 'src/app/core/services/post.service';
import { NewComment } from 'src/app/models/comment.model';

@Component({
  selector: 'app-create-comment',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-comment.component.html'
})
export class CreateCommentComponent {

  @Input() postId!: number;
  @Output() commentCreated = new EventEmitter<void>();

  form: FormGroup;
  submitting = false;

  constructor(private fb: FormBuilder, private postService: PostService) {
    this.form = this.fb.group({
      content: ['', [Validators.required, Validators.minLength(1)]]
    });
  }

  submit(): void {
    
    if (this.form.invalid || this.submitting) return;

    this.submitting = true;

    const payload: NewComment = { content: this.form.value.content };
console.log('POST payload:', payload, 'url:', `/api/post/${this.postId}/comment`);

    this.postService.createComment(this.postId, payload).subscribe({
      next: (newComment) => {
        this.submitting = false;
        this.form.reset();
        this.commentCreated.emit();
      },
      error: (err) => {
        this.submitting = false;
        console.error(err);
      }
    });
  }
}
