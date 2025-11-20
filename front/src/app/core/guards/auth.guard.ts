import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

/**
 * Authentication guard that protects routes requiring user authentication.
 * 
 * This guard checks if the user is authenticated by calling the AuthService.
 * It performs an asynchronous authentication check and returns an Observable.
 * 
 * Behavior:
 * - If authenticated: Allows access to the route (returns true)
 * - If not authenticated: Redirects to the login page (returns UrlTree to /login)
 * 
 * The guard uses the functional approach (CanActivateFn) introduced in Angular 15+.
 * 
 * @param route - The activated route snapshot being accessed
 * @param state - The router state snapshot
 * @returns Observable<boolean | UrlTree> that emits true if authenticated, or a UrlTree for redirection
 * 
 * @example
 * ```typescript
 * // In route configuration:
 * {
 *   path: 'home',
 *   component: HomeComponent,
 *   canActivate: [authGuard]
 * }
 * ```
 */
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.isAuthenticated().pipe(
    map(isAuth => {
      if (isAuth) return true;
      return router.createUrlTree(['/login']);
    })
  );
};
