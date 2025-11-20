import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { MatSidenav } from '@angular/material/sidenav';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';

/**
 * Navigation bar component that adapts to desktop and mobile layouts.
 * 
 * This component provides responsive navigation with:
 * - Desktop: Horizontal navigation bar with links
 * - Mobile: Hamburger menu with side navigation drawer
 * 
 * The navbar visibility is controlled based on the current route and device type,
 * hiding on authentication pages (login, register, landing) when on mobile devices.
 * 
 * Key features:
 * - Responsive design using Angular CDK BreakpointObserver
 * - Authentication state management
 * - Automatic navigation drawer closing after logout
 * - Dynamic navbar visibility based on route and device type
 */
@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, MatIconModule, MatButtonModule, MatSidenavModule, MatToolbarModule, MatListModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  /** Reference to the Material sidenav component for mobile navigation */
  @ViewChild('sidenav') sidenav!: MatSidenav;
  /** Flag indicating if the user is currently logged in */
  isLoggedIn = false;
  /** Flag indicating if the current viewport is mobile size */
  isMobile = false;
  /** Flag controlling navbar visibility based on route and device */
  showNavbar = true;

  constructor(
    private authService: AuthService,
    private router: Router,
    private breakpointObserver: BreakpointObserver
  ) {}

  /**
   * Initializes the component and sets up subscriptions.
   * 
   * Sets up:
   * - Authentication state monitoring
   * - Breakpoint observer for responsive behavior
   * - Router events subscription for navbar visibility updates
   */
  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.authService.authChanged$.subscribe(
      (status) => (this.isLoggedIn = status)
    );

    this.breakpointObserver.observe([Breakpoints.Handset])
      .subscribe(result => {
        this.isMobile = result.matches;
        this.updateNavbarVisibility();
      });

    this.router.events.subscribe(() => {
      this.updateNavbarVisibility();
    });
  }

  /**
   * Updates navbar visibility based on current route and device type.
   * 
   * Hides the navbar on mobile devices when on authentication routes
   * (login, register, landing page), otherwise shows it.
   */
  updateNavbarVisibility(): void {
    const currentUrl = this.router.url;
    const authRoutes = ['/login', '/register', '/'];
    if (this.isMobile && authRoutes.includes(currentUrl)) {
      this.showNavbar = false;
    } else {
      this.showNavbar = true;
    }
  }

  /**
   * Logs out the current user and redirects to the login page.
   * 
   * Performs the following actions:
   * - Calls the authentication service to logout
   * - Navigates to the login page
   * - Closes the mobile sidenav if open
   */
  logout(): void {
    this.authService.logout().subscribe(() => {
      this.router.navigate(['/login']);
      if (this.isMobile && this.sidenav) {
        this.sidenav.close();
      }
    });
  }

  /**
   * Closes the mobile navigation sidenav.
   * 
   * Only performs the action if:
   * - The device is in mobile mode
   * - The sidenav component is available
   */
  closeSidenav(): void {
    if (this.isMobile && this.sidenav) {
      this.sidenav.close();
    }
  }
}
