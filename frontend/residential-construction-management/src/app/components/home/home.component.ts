import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs'; 
import { AuthService } from '../../core/services/auth.service'; 

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  
  isLoggedIn$: Observable<boolean>; 

  constructor(private authService: AuthService) { 
    this.isLoggedIn$ = this.authService.isLoggedIn; 
  }
  

  ngOnInit(): void {
  }

}