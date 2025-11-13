import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable, map, tap } from 'rxjs';
import { FeedPost, Feed } from 'src/app/models/post.model';

@Injectable({
  providedIn: 'root'
})
export class FeedService {
  private feedSubject = new BehaviorSubject<FeedPost[]>([]);
  feed$ = this.feedSubject.asObservable();

  private currentPage = 0;
  private pageSize = 10;
  private lastPage = false;
  sortAsc = false;

  constructor(private http: HttpClient) {}

  loadNextPage(): Observable<FeedPost[]> {
    if (this.lastPage) return this.feed$;

    const sortParam = this.sortAsc ? 'createdAt,asc' : 'createdAt,desc';

    const params = new HttpParams()
      .set('page', this.currentPage.toString())
      .set('size', this.pageSize.toString())
      .set('sort', sortParam);

    return this.http.get<Feed>('/api/feed', { params, withCredentials: true }).pipe(
      tap(resp => {
        this.currentPage = resp.number + 1;
        this.lastPage = resp.last;
        this.feedSubject.next([...this.feedSubject.value, ...resp.content]);
      }),
      map(resp => resp.content)
    );
  }

  resetFeed() {
    this.currentPage = 0;
    this.lastPage = false;
    this.feedSubject.next([]);
  }

  toggleSort() {
    this.sortAsc = !this.sortAsc;
    this.resetFeed();
  }
}
