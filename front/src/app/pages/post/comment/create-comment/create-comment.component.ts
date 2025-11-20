import { Component, EventEmitter, Input, OnDestroy, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PostService } from 'src/app/core/services/post.service';
import { NewComment } from 'src/app/models/comment.model';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Subscription } from 'rxjs';

/**
 * Comment creation component.
 * Allows users to add a comment to an post.
 */
@Component({
  selector: 'app-create-comment',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatInputModule, MatButtonModule, MatIconModule],
  templateUrl: './create-comment.component.html',
  styleUrls: ['./create-comment.component.scss']
})
export class CreateCommentComponent implements OnDestroy {
  /** Identifier of the post to which the comment will be added */
  @Input() postId!: number;
  /** Event emitted after successful comment creation */
  @Output() commentCreated = new EventEmitter<void>();

  /** Comment creation form */
  form: FormGroup;
  /** Form submission indicator */
  submitting = false;
  /** Subscription management to prevent memory leaks */
  private subscription?: Subscription;

  constructor(private fb: FormBuilder, private postService: PostService) {
    this.form = this.fb.group({
      content: ['', [Validators.required, Validators.minLength(1)]]
    });
  }

  /**
   * Submits the comment creation form.
   * On success, resets the form and emits the commentCreated event.
   */
  submit(): void {
    
    if (this.form.invalid || this.submitting) return;

    this.submitting = true;

    const payload: NewComment = { content: this.form.value.content };

    this.subscription = this.postService.createComment(this.postId, payload).subscribe({
      next: (newComment) => {
        this.submitting = false;
        this.form.reset();
        this.commentCreated.emit();
      },
      error: () => {
        this.submitting = false;
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
