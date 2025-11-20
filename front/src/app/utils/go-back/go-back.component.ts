import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';

/**
 * Component providing a "go back" navigation button.
 * 
 * This component displays a back button that navigates to:
 * - Home page (/home) if the user is logged in
 * - Landing page (/) if the user is not logged in
 * 
 * Used in header sections to provide consistent navigation across the application.
 */
@Component({
  selector: 'app-go-back',
  imports: [MatIconModule],
  templateUrl: './go-back.component.html',
  styleUrl: './go-back.component.scss'
})
export class GoBackComponent {

  constructor(private router: Router, private authService: AuthService) {}

  /**
   * Navigates back to the appropriate page based on authentication status.
   * 
   * Redirects to:
   * - /home if user is authenticated
   * - / (landing page) if user is not authenticated
   */
  goBack() {
    if (this.authService.isLoggedIn()) {
      this.router.navigateByUrl('/home');
    } else {
      this.router.navigateByUrl('/');
    }
  }
}
