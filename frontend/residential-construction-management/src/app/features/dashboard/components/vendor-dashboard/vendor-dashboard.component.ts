import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service'; 

@Component({
  selector: 'app-vendor-dashboard',
  templateUrl: './vendor-dashboard.component.html',
  styleUrls: ['./vendor-dashboard.component.scss']
})
export class VendorDashboardComponent implements OnInit {

  username: string | null = null; 

  constructor(private authService: AuthService) { } 

  ngOnInit(): void {
    const user = this.authService.getUser(); 
    if (user) {
      this.username = user.username; 
    }
  }

}