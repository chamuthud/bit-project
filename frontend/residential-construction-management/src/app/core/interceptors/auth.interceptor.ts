import { Injectable, Injector } from '@angular/core'; 
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HTTP_INTERCEPTORS 
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service'; 


const API_URL = 'http://localhost:8080/api'; 

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  
  constructor(private injector: Injector) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    
    const authService = this.injector.get(AuthService);
    const token = authService.getToken(); 
    

    const isApiUrl = request.url.startsWith(API_URL); 

    
    if (token && isApiUrl) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(request);
  }
}


export const authInterceptorProvider = [
  { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
];

