import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service'; 
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerForm: FormGroup;
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';
  submitted = false;
  isLoading = false;


  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(100)]],
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(120)]],
      firstName: ['', [Validators.required, Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.maxLength(50)]],
      nic: ['', [Validators.maxLength(15)]], 
      mobile: ['', [Validators.maxLength(20)]], 
      address: [''], 
      // --- ADDED Form Control ---
      role: ['', [Validators.required]] 
      
    });
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.registerForm.valid && !this.isLoading) { 
      this.isLoading = true; 
      this.isSignUpFailed = false;
      this.errorMessage = '';

      
      this.authService.register(this.registerForm.value).subscribe({
        next: data => {
          this.isLoading = false; 
          console.log(data);
          this.isSuccessful = true;
          
        },
        error: err => {
          this.isLoading = false; 
         
          this.errorMessage = err?.error?.message || err.message || 'Registration failed. Please try again.';
          this.isSignUpFailed = true;
          console.error('Registration error:', err);
        }
      });
     } else if (!this.registerForm.valid) {
        this.isSignUpFailed = true;
        this.errorMessage = 'Please fill in all required fields correctly.';
        this.registerForm.markAllAsTouched();
     }
  }

  // --- Getters for template validation ---
  get username() { return this.registerForm.get('username'); }
  get email() { return this.registerForm.get('email'); }
  get password() { return this.registerForm.get('password'); }
  get firstName() { return this.registerForm.get('firstName'); }
  get lastName() { return this.registerForm.get('lastName'); }
  get nic() { return this.registerForm.get('nic'); }
  get mobile()  { return this.registerForm.get('mobile'); }
  get role() { return this.registerForm.get('role'); }
  
}