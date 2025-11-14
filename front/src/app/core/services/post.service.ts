import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DetailedPost, NewPost } from 'src/app/models/post.model';
import { DetailedComment, NewComment, Page } from 'src/app/models/comment.model';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private http: HttpClient) {}

  getPost(id: number): Observable<DetailedPost> {
    return this.http.get<DetailedPost>(`/api/post/${id}`, {
      withCredentials: true
    });
  }

  createPost(data: NewPost): Observable<DetailedPost> {
    return this.http.post<DetailedPost>('/api/post', data, {
      withCredentials: true
    });
  }

  getComments(postId: number): Observable<Page<DetailedComment>> {
    return this.http.get<Page<DetailedComment>>(`/api/post/${postId}/comments`, {
      withCredentials: true
    });
  }

  createComment(postId: number, data: NewComment): Observable<DetailedComment> {
    return this.http.post<DetailedComment>(`/api/post/${postId}/comment`, data, {
      withCredentials: true
    });
  }
}
