import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Observable, Subject, takeUntil, tap, switchMap, catchError, of, BehaviorSubject } from 'rxjs';
import { User, UserUpdate } from 'src/app/models/user.model';
import { Topic } from 'src/app/models/topic.model';
import { UserService } from 'src/app/core/services/user.service';
import { TopicService } from 'src/app/core/services/topic.service';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { passwordStrengthValidator } from 'src/app/utils/validators/passwordStrengthValidator';
import { MatDividerModule } from '@angular/material/divider';

/**
 * User profile management component.
 * Allows users to modify their personal information and manage their topic subscriptions.
 */
@Component({
  selector: 'app-user-details',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatInputModule, MatFormFieldModule, ReactiveFormsModule, MatDividerModule, MatCardModule],
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.scss']
})
export class UserDetailsComponent implements OnInit, OnDestroy {
  /** Profile modification form */
  form!: FormGroup;
  /** Observable of the current user */
  user$!: Observable<User>;
  /** Observable of the user's subscriptions */
  subscriptions$: Observable<Topic[]>;
  
  /** Subject to manage subscription destruction */
  private destroy$ = new Subject<void>();
  /** BehaviorSubject to manage subscription state */
  private subscriptionsSubject = new BehaviorSubject<Topic[]>([]);
  
  /** Error message to display */
  errorMessage: string | null = null;
  /** Success message to display */
  successMessage: string | null = null;

  constructor(
    private fb: FormBuilder, 
    private userService: UserService, 
    private topicService: TopicService
  ) {
    this.subscriptions$ = this.subscriptionsSubject.asObservable();
  }

  /**
   * Conditional validator for password.
   * Applies password strength validation only if a password is provided.
   * @returns Validation function
   */
  conditionalPasswordValidator(): any {
    return (control: any) => {
      const value = control.value;
      if (!value || value.length === 0) {
        return null;
      }
      return passwordStrengthValidator(control);
    };
  }

  /**
   * Initializes the component by creating the form and loading
   * the user's information and subscriptions.
   */
  ngOnInit(): void {
    this.form = this.fb.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [this.conditionalPasswordValidator()]]
    });

    this.user$ = this.userService.getUser().pipe(
      tap(user => {
        this.form.patchValue({
          username: user.username,
          email: user.email
        }, { emitEvent: false });
      }),
      catchError(() => {
        this.errorMessage = 'Unable to load user information';
        return of({ username: '', email: '' } as User);
      }),
      takeUntil(this.destroy$)
    );

    this.user$.subscribe();

    this.topicService.getUserTopics().pipe(
      tap(topics => this.subscriptionsSubject.next(topics)),
      catchError(() => {
        this.errorMessage = 'Unable to load your subscriptions';
        return of([]);
      }),
      takeUntil(this.destroy$)
    ).subscribe();
  }

  /**
   * Submits the profile modification form.
   * Updates the user's information (username, email, and optionally password).
   */
  onSubmit(): void {
    if (this.form.invalid) {
      this.errorMessage = 'Veuillez remplir correctement tous les champs';
      return;
    }

    this.errorMessage = null;
    this.successMessage = null;

    const editData: UserUpdate = {
      username: this.form.value.username,
      email: this.form.value.email,
      ...(this.form.value.password ? { password: this.form.value.password } : {})
    };

    this.userService.updateUser(editData).pipe(
      tap(updatedUser => {
        this.successMessage = 'Profil mis à jour avec succès';
        this.form.patchValue({ password: '' }, { emitEvent: false });
      }),
      catchError(() => {
        this.errorMessage = 'Erreur lors de la mise à jour du profil';
        return of(null);
      }),
      takeUntil(this.destroy$)
    ).subscribe();
  }

  /**
   * Unsubscribes the user from a topic.
   * Reloads the subscription list after unsubscribing.
   * @param topicId - The identifier of the topic to unsubscribe from
   */
  onUnsubscribe(topicId: number): void {
    this.topicService.unsubscribeFromTopic(topicId).pipe(
      switchMap(() => this.topicService.getUserTopics()),
      tap(topics => {
        this.subscriptionsSubject.next(topics);
        this.successMessage = 'Successfully unsubscribed';
      }),
      catchError(() => {
        this.errorMessage = 'Error during unsubscription';
        return of([]);
      }),
      takeUntil(this.destroy$)
    ).subscribe();
  }

  /**
   * Cleans up subscriptions when the component is destroyed.
   */
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
