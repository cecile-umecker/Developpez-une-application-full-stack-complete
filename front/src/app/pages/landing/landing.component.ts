import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';

/**
 * Landing page component.
 * Public home page displayed to unauthenticated users.
 * Contains links to login and registration.
 */
@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [RouterModule, CommonModule, MatButtonModule],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})
export class LandingComponent {

}
