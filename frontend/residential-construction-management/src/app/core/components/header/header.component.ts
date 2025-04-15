import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs'; 
import { AuthService } from '../../services/auth.service'; 
import { Router } from '@angular/router'; 

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy { 
  isLoggedIn = false;
  username: string | null = null;
  userRole: string | null = null; 
  private authSubscription: Subscription | undefined; 

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    
    this.authSubscription = this.authService.isLoggedIn.subscribe(status => {
      this.isLoggedIn = status;
      if (this.isLoggedIn) {
         const user = this.authService.getUser();
         this.username = user ? user.username : null;
         this.userRole = user ? user.role : null; 
      } else {
        this.username = null;
        this.userRole = null; 
      }
    });
  }

  ngOnDestroy(): void {
    this.authSubscription?.unsubscribe();
  }

  
  get dashboardLink(): string {
    switch (this.userRole) {
      case 'ROLE_ADMIN': return '/dashboard/admin';
      case 'ROLE_CLIENT': return '/dashboard/client';
      case 'ROLE_CONTRACTOR': return '/dashboard/contractor';
      case 'ROLE_VENDOR': return '/dashboard/vendor';
      default: return '/'; 
    }
  }
  


  logout(): void {
    this.authService.logout();
  }
}