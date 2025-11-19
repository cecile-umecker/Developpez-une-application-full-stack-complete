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

@Component({
  selector: 'app-user-details',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatInputModule, MatFormFieldModule, ReactiveFormsModule, MatDividerModule, MatCardModule],
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.scss']
})
export class UserDetailsComponent implements OnInit, OnDestroy {

  form!: FormGroup;
  user$!: Observable<User>;
  subscriptions$: Observable<Topic[]>;
  
  private destroy$ = new Subject<void>();
  private subscriptionsSubject = new BehaviorSubject<Topic[]>([]);
  
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(
    private fb: FormBuilder, 
    private userService: UserService, 
    private topicService: TopicService
  ) {
    // Make subscriptions$ an observable of the BehaviorSubject
    this.subscriptions$ = this.subscriptionsSubject.asObservable();
  }

  conditionalPasswordValidator(): any {
    return (control: any) => {
      const value = control.value;
      // If empty, no validation error
      if (!value || value.length === 0) {
        return null;
      }
      // If not empty, apply password strength validation
      return passwordStrengthValidator(control);
    };
  }

  ngOnInit(): void {
    // Initialize reactive form with validators
    this.form = this.fb.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [this.conditionalPasswordValidator()]]
    });

    // Load user data and populate form
    this.user$ = this.userService.getUser().pipe(
      tap(user => {
        this.form.patchValue({
          username: user.username,
          email: user.email
        }, { emitEvent: false });
      }),
      catchError(err => {
        console.error('Erreur récupération user', err);
        this.errorMessage = 'Impossible de charger les informations utilisateur';
        return of({ username: '', email: '' } as User);
      }),
      takeUntil(this.destroy$)
    );

    // Subscribe to user$ to trigger the HTTP request
    this.user$.subscribe();

    // Load user's topic subscriptions
    this.topicService.getUserTopics().pipe(
      tap(topics => this.subscriptionsSubject.next(topics)),
      catchError(err => {
        console.error('Erreur récupération topics', err);
        this.errorMessage = 'Impossible de charger vos abonnements';
        return of([]);
      }),
      takeUntil(this.destroy$)
    ).subscribe();
  }

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
        // Clear password field after successful update
        this.form.patchValue({ password: '' }, { emitEvent: false });
      }),
      catchError(err => {
        console.error('Erreur mise à jour profil', err);
        this.errorMessage = 'Erreur lors de la mise à jour du profil';
        return of(null);
      }),
      takeUntil(this.destroy$)
    ).subscribe();
  }

  onUnsubscribe(topicId: number): void {
    this.topicService.unsubscribeFromTopic(topicId).pipe(
      switchMap(() => this.topicService.getUserTopics()),
      tap(topics => {
        this.subscriptionsSubject.next(topics);
        this.successMessage = 'Désabonnement réussi';
      }),
      catchError(err => {
        console.error('Erreur désabonnement', err);
        this.errorMessage = 'Erreur lors du désabonnement';
        return of([]);
      }),
      takeUntil(this.destroy$)
    ).subscribe();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
