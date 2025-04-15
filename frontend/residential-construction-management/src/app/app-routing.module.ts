import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard'; 
import { RoleGuard } from './core/guards/role.guard';

import { HomeComponent } from './components/home/home.component'; 

const routes: Routes = [
  { path: '', component: HomeComponent }, 
  { path: 'home', component: HomeComponent }, 
  

  
  {
    path: 'auth',
    loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule)
    
  },

  
  {
    path: 'projects',
    loadChildren: () => import('./features/project/project.module').then(m => m.ProjectModule),
    canActivate: [AuthGuard] 
  },

  {
    path: 'vendor',
    loadChildren: () => import('./features/vendor/vendor.module').then(m => m.VendorModule),
    canActivate: [AuthGuard, RoleGuard], 
    data: { expectedRoles: ['ROLE_VENDOR', 'ROLE_ADMIN'] }
  },

  {
    path: 'dashboard',
    loadChildren: () => import('./features/dashboard/dashboard.module').then(m => m.DashboardModule),
    canActivate: [AuthGuard] 
  },

  
  { path: 'login', redirectTo: '/auth/login', pathMatch: 'full' },
  { path: 'register', redirectTo: '/auth/register', pathMatch: 'full' },

  
  { path: '**', redirectTo: '' } 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }