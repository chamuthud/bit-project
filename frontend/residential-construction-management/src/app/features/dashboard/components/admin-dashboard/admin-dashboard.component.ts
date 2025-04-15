import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {

  username: string | null = null; 

  constructor(private authService: AuthService) { } 

  ngOnInit(): void {
    const user = this.authService.getUser(); 
    if (user) {
      this.username = user.username; 
    }
  }

}