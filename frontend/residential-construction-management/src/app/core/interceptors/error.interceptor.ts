import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse, 
  HTTP_INTERCEPTORS
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs'; 
import { catchError } from 'rxjs/operators'; 
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';


@Injectable()
export class ErrorInterceptor implements HttpInterceptor {


  constructor(private router: Router, private authService: AuthService) {} 


  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'An unknown error occurred!';


        if (error.error instanceof ErrorEvent) {
          // Client-side error
          errorMessage = `Error: ${error.error.message}`;
        } else {
          // Server-side error
          errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;


          // Handle specific status codes
          if (error.status === 401) { // Unauthorized
             console.error('Unauthorized access - Redirecting to login');
             this.authService.logout(); // Perform logout actions
             // this.router.navigate(['/auth/login']);
             errorMessage = 'Your session may have expired. Please login again.';


          } else if (error.status === 403) { // Forbidden
             console.error('Forbidden access');
              errorMessage = 'You do not have permission to access this resource.';


          } else if (error.status === 404) { // Not Found
            console.error('Resource not found');
            errorMessage = 'The requested resource could not be found.';
          }
           else if (error.status === 400) { // Bad Request (e.g., validation)
             console.error('Bad Request:', error.error);
             // Try to extract validation errors if backend sends them structured
             if (typeof error.error === 'object' && error.error !== null) {
                 const messages = Object.values(error.error).join(', ');
                 errorMessage = `Validation failed: ${messages}`;
             } else {
                 errorMessage = error.error?.message || 'Invalid data submitted.';
             }
          }
          // Add handling for other specific errors (500, etc.)
        }


        // Log the error (or use a proper logging service)
        console.error(errorMessage);


         // Display the error to the user (use a Snackbar/Toast service in real app)
         alert(errorMessage); // Simple alert for demonstration


        return throwError(() => new Error(errorMessage)); // Re-throw the error
      })
    );
  }
}


// Export the provider configuration
export const errorInterceptorProvider = [
  { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
];