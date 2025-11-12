import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from 'src/app/models/user.model';
import { AuthResponse, LoginCredentials, RegisterData } from 'src/app/models/auth.model';
import { BehaviorSubject, catchError, map, Observable, of, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private loggedIn = false;
  private currentUser?: User;
  private authChanged = new BehaviorSubject<boolean>(this.isLoggedIn());
  authChanged$ = this.authChanged.asObservable();

  constructor(private http: HttpClient) { }

  login(credentials: LoginCredentials): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('/api/auth/login', credentials, { withCredentials: true }).pipe(
      tap(() => {
        this.loggedIn = true;
        localStorage.setItem('loggedIn', 'true');
        this.authChanged.next(true);
      })
    );
  }

  register(data: RegisterData): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('/api/auth/register', data, { withCredentials: true });
  }

  logout(): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('/api/auth/logout', {}, { withCredentials: true }).pipe(
      tap(() => {
        localStorage.removeItem('loggedIn');
        this.loggedIn = false;
        this.currentUser = undefined;
        this.authChanged.next(false);
      })
    );
  }

  isAuthenticated(): Observable<boolean> {
    return this.http.get<User>('/api/user/me', { withCredentials: true }).pipe(
      tap(user => {
        localStorage.setItem('loggedIn', 'true');
        this.loggedIn = true;
        this.currentUser = user;
        this.authChanged.next(true);
      }),
      map(() => true),
      catchError(() => {
        localStorage.removeItem('loggedIn');
        this.loggedIn = false;
        this.authChanged.next(false);
        return of(false);
      })
    );
  }

  isLoggedIn(): boolean {
    return this.loggedIn || localStorage.getItem('loggedIn') === 'true';
  }
}
