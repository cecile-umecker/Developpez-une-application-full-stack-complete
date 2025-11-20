import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';

/**
 * Local authentication guard that protects routes using synchronous authentication check.
 * 
 * This guard performs a synchronous check of the user's authentication status
 * by verifying local storage or session state. Unlike authGuard, this does not
 * make an HTTP request to validate the authentication.
 * 
 * Behavior:
 * - If logged in (local state): Allows access to the route (returns true)
 * - If not logged in: Redirects to the landing page (returns UrlTree to /)
 * 
 * Use this guard when you need faster, synchronous authentication checks that
 * don't require server validation.
 * 
 * @param route - The activated route snapshot being accessed
 * @param state - The router state snapshot
 * @returns boolean | UrlTree - true if authenticated, or UrlTree for redirection to landing page
 * 
 * @example
 * ```typescript
 * // In route configuration:
 * {
 *   path: 'profile',
 *   component: ProfileComponent,
 *   canActivate: [localAuthGuard]
 * }
 * ```
 */
export const localAuthGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  if (!authService.isLoggedIn()) {
    return router.createUrlTree(['/']);
  }
  return true;
};
