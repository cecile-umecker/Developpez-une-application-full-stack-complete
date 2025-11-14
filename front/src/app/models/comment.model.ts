export interface NewComment {
  content: string;
}

export interface Page<T> {
  content: T[];
  pageable: any;
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  sort: any;
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}

export interface DetailedComment {
  id: number;
  authorName: string;
  content: string;
  createdAt: Date;
}