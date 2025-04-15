import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http'; 

import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { authInterceptorProvider } from './interceptors/auth.interceptor'; 
import { errorInterceptorProvider } from './interceptors/error.interceptor'; 


@NgModule({
  declarations: [
    HeaderComponent,
    FooterComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    HttpClientModule 
  ],
  exports: [
    HeaderComponent,
    FooterComponent
  ],
  providers: [
     authInterceptorProvider, 
     errorInterceptorProvider
  ]
})
export class CoreModule { }
