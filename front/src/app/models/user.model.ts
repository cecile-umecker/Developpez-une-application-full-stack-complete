export interface User {
  username: string;
  email: string;
}

export interface UserUpdate {
  username?: string;
  email?: string;
  password?: string;
}