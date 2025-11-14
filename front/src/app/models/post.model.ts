export interface FeedPost {
  id: number;
  title: string;
  authorName: string;
  contentPreview: string;
  createdAt: string;
}

export interface Pageable {
  pageNumber: number;
  pageSize: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  offset: number;
  paged: boolean;
  unpaged: boolean;
}

export interface Feed {
  content: FeedPost[];
  pageable: Pageable;
  last: boolean;
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}

export interface DetailedPost {
  id: number;
  title: string;
  content: string;
  authorName: string;
  topicTitle: string;
  createdAt: Date;
}

export interface NewPost {
  title: number;
  content: string;
  topicId: number;
}