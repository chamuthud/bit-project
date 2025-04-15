import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators'; 
import { Router } from '@angular/router'; 


interface LoginRequest {
  username?: string | null;
  password?: string | null;
}

interface SignupRequest {
  username?: string | null;
  email?: string | null;
  password?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  nic?: string | null;
  mobile?: string | null;
  address?: string | null;
}

interface JwtResponse {
  token: string;
  type: string;
  id: number;
  username: string;
  email: string;
  role: string;
}

interface MessageResponse {
  message: string;
}

const AUTH_API = 'http://localhost:8080/api/auth'; 

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};


const USER_KEY = 'auth-user';


@Injectable({
  providedIn: 'root' 
})
export class AuthService {

  
  private loggedIn = new BehaviorSubject<boolean>(this.hasToken());
  isLoggedIn = this.loggedIn.asObservable(); 


  constructor(private http: HttpClient, private router: Router) { }

  login(credentials: LoginRequest): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(`${AUTH_API}/signin`, credentials, httpOptions).pipe(
        tap(response => this.saveUser(response)) 
    );
  }

  register(user: SignupRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(`${AUTH_API}/signup`, user, httpOptions);
  }

  logout(): void {
    window.sessionStorage.removeItem(USER_KEY);
    this.loggedIn.next(false); 
    this.router.navigate(['/auth/login']); 
  }

   

  public saveUser(user: JwtResponse): void {
    window.sessionStorage.removeItem(USER_KEY); 
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user)); 
    this.loggedIn.next(true); 
  }

  public getUser(): JwtResponse | null {
    const user = window.sessionStorage.getItem(USER_KEY); 
    if (user) {
      return JSON.parse(user);
    }
    return null;
  }

   public getToken(): string | null {
    const user = this.getUser();
    return user ? user.token : null;
  }

  private hasToken(): boolean {
    return !!window.sessionStorage.getItem(USER_KEY);
  }

  public hasRole(role: string): boolean {
    const user = this.getUser();
    return !!user && user.role === role;
  }
}