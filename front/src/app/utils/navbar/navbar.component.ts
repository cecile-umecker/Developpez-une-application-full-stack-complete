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

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, MatIconModule, MatButtonModule, MatSidenavModule, MatToolbarModule, MatListModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  @ViewChild('sidenav') sidenav!: MatSidenav;
  isLoggedIn = false;
  isMobile = false;
  showNavbar = true;

  constructor(
    private authService: AuthService,
    private router: Router,
    private breakpointObserver: BreakpointObserver
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.authService.authChanged$.subscribe(
      (status) => (this.isLoggedIn = status)
    );

    this.breakpointObserver.observe([Breakpoints.Handset])
      .subscribe(result => {
        this.isMobile = result.matches;
        console.log('isMobile:', this.isMobile);
        this.updateNavbarVisibility();
      });

    this.router.events.subscribe(() => {
      this.updateNavbarVisibility();
    });
  }

  updateNavbarVisibility(): void {
    const currentUrl = this.router.url;
    const authRoutes = ['/login', '/register', '/'];
    if (this.isMobile && authRoutes.includes(currentUrl)) {
      this.showNavbar = false;
    } else {
      this.showNavbar = true;
    }
  }

  logout(): void {
    this.authService.logout().subscribe(() => {
      this.router.navigate(['/login']);
      if (this.isMobile && this.sidenav) {
        this.sidenav.close();
      }
    });
  }

  closeSidenav(): void {
    if (this.isMobile && this.sidenav) {
      this.sidenav.close();
    }
  }
}
