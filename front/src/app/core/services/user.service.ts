import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User, UserUpdate } from 'src/app/models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {}

  // récupère les infos du user connecté
  getUser(): Observable<User> {
    return this.http.get<User>('/api/user/me', { withCredentials: true });
  }

  // met à jour le user connecté
  updateUser(data: UserUpdate): Observable<User> {
    return this.http.put<User>('/api/user/me', data, { withCredentials: true });
  }
}
