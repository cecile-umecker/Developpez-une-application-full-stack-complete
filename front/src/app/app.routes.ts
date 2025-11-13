import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { localAuthGuard } from './core/guards/local-auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/landing/landing.component').then(m => m.LandingComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./pages/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./pages/auth/register/register.component').then(m => m.RegisterComponent)
  },
  {
    path: 'home',
    loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent),
    canActivate: [authGuard]
  },
  {
    path: 'topics',
    loadComponent: () => import('./pages/topics/topics.component').then(m => m.TopicsComponent),
    canActivate: [localAuthGuard]
  },
  {
    path: 'profile',
    loadComponent: () => import('./pages/user-details/user-details.component').then(m => m.UserDetailsComponent),
    canActivate: [localAuthGuard]
  },
  {
    path: 'postDetails/:id',
    loadComponent: () => import('./pages/post/post.component').then(m => m.PostComponent),
    canActivate: [localAuthGuard]
  },
  {
    path: 'post/create',
    loadComponent: () => import('./pages/post/create-post/create-post.component').then(m => m.CreatePostComponent),
    canActivate: [localAuthGuard]
  },
  { path: '**', redirectTo: '' }
];
