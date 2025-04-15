import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service'; 

@Component({
  selector: 'app-client-dashboard',
  templateUrl: './client-dashboard.component.html',
  styleUrls: ['./client-dashboard.component.scss']
})
export class ClientDashboardComponent implements OnInit {

  username: string | null = null; 

  constructor(private authService: AuthService) { } 

  ngOnInit(): void {
    const user = this.authService.getUser(); 
    if (user) {
      this.username = user.username; 
    }
  }

}