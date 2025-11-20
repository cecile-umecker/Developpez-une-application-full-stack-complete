import { CommonModule } from '@angular/common';
import { Component, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { GoBackComponent } from 'src/app/utils/go-back/go-back.component';
import { passwordStrengthValidator } from 'src/app/utils/validators/passwordStrengthValidator';
import { Subscription } from 'rxjs';

/**
 * User registration component.
 * Manages the registration form with validation for username, email, and strong password.
 */
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, GoBackComponent, MatButtonModule, MatInputModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnDestroy {
  /** Registration form with username, email, and password fields */
  registerForm: FormGroup;
  /** Error message to display on failure */
  errorMessage?: string;
  /** Loading indicator during registration */
  loading = false;
  /** Subscription management to prevent memory leaks */
  private subscription?: Subscription;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, passwordStrengthValidator]]
    });
  }

  /**
   * Submits the registration form.
   * On success, redirects to the login page.
   * On failure, displays an error message.
   */
  onSubmit(): void {
    if(this.registerForm.invalid) return;

    this.loading = true;
    this.errorMessage = undefined;

    this.subscription = this.authService.register(this.registerForm.value).subscribe({
      next: () => this.router.navigate(['/login']),
      error: err => {
        this.errorMessage = err.error?.message || 'Erreur lors de l\'inscription';
        this.loading = false;
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
