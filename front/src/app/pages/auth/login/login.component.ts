import { Component, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from 'src/app/core/services/auth.service';
import { GoBackComponent } from 'src/app/utils/go-back/go-back.component';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { Subscription } from 'rxjs';

/**
 * User login component.
 * Manages the login form with validation and authentication.
 */
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, GoBackComponent, MatButtonModule, MatInputModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnDestroy {
  /** Login form with login and password fields */
  loginForm: FormGroup;
  /** Error message to display on failure */
  errorMessage?: string;
  /** Loading indicator during authentication */
  loading = false;
  /** Subscription management to prevent memory leaks */
  private subscription?: Subscription;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      login: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  /**
   * Submits the login form.
   * On success, redirects to the home page.
   * On failure, displays an error message.
   */
  onSubmit(): void {
    if (this.loginForm.invalid) return;

    this.loading = true;
    this.errorMessage = undefined;

    this.subscription = this.authService.login(this.loginForm.value).subscribe({
      next: () => this.router.navigate(['/home']),
      error: err => {
        this.errorMessage = err.error?.message || 'Erreur de connexion';
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
