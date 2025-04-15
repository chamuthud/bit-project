import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms'; // Import reactive forms modules
import { AuthService } from '../../../core/services/auth.service'; // Correct path
import { Router } from '@angular/router'; // Import Router

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  submitted = false;
  isLoading = false;
  // role = ''; // No longer needed here directly

  constructor(
    private fb: FormBuilder,
    private authService: AuthService, // Ensure AuthService is injected
    private router: Router // Ensure Router is injected
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    // If already logged in, maybe redirect away from login page
     if (this.authService.getUser()) {
       // Redirect to appropriate dashboard if already logged in
       this.navigateToDashboard();
       console.log('User already logged in, redirecting to dashboard');
     }
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.loginForm.valid && !this.isLoading) { // Check !isLoading
      this.isLoading = true; // Set loading true
      this.isLoginFailed = false;
      this.errorMessage = '';

      this.authService.login(this.loginForm.value).subscribe({
        next: data => {
          this.isLoading = false; // Set loading false
          this.isLoggedIn = true;
          console.log('Login successful:', data);
          // --- MODIFIED NAVIGATION ---
          // Navigate based on role after successful login
          this.navigateToDashboard();
          // --- END MODIFIED NAVIGATION ---
        },
        error: err => {
          this.isLoading = false; // Set loading false
          // Try to get error message from backend response first
          this.errorMessage = err?.error?.message || err.message || 'Login failed. Please check credentials.';
          this.isLoginFailed = true;
          console.error('Login error:', err);
        }
      });
    } else if (!this.loginForm.valid) {
        this.isLoginFailed = true;
        this.errorMessage = 'Please fill in both username and password.';
        this.loginForm.markAllAsTouched(); // Ensure errors show if not touched yet
    }
  }

  // --- ADDED HELPER METHOD ---
  navigateToDashboard(): void {
    const user = this.authService.getUser();
    if (!user) {
      this.router.navigate(['/auth/login']); // Fallback if user somehow not available
      return;
    }

    let dashboardRoute = '/'; // Default fallback

    switch (user.role) {
      case 'ROLE_ADMIN':
        dashboardRoute = '/dashboard/admin';
        break;
      case 'ROLE_CLIENT':
        dashboardRoute = '/dashboard/client';
        break;
      case 'ROLE_CONTRACTOR':
        dashboardRoute = '/dashboard/contractor';
        break;
      case 'ROLE_VENDOR':
        dashboardRoute = '/dashboard/vendor';
        break;
      default:
        // Handle unknown roles or redirect to a generic page
        console.warn(`Unknown user role: ${user.role}. Redirecting to home.`);
        dashboardRoute = '/'; // Or maybe '/projects'
    }
    this.router.navigate([dashboardRoute]);
  }
  // --- END ADDED HELPER METHOD ---

  // Helper for template validation messages
  get username() { return this.loginForm.get('username'); }
  get password() { return this.loginForm.get('password'); }
}