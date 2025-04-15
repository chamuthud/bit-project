import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service'; 

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot, 
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    
    const expectedRoles: string[] = route.data['expectedRoles'];

    if (!expectedRoles || expectedRoles.length === 0) {
      console.warn('RoleGuard requires expectedRoles in route data.');
      return false; 
    }

    const user = this.authService.getUser();

    if (!user || !user.role) {
      
      return this.router.createUrlTree(['/auth/login'], { queryParams: { returnUrl: state.url } });
    }

    
    const hasRequiredRole = expectedRoles.includes(user.role);

    if (hasRequiredRole) {
      return true; 
    } else {
      
      console.warn(`Access denied for role "${user.role}". Required roles: ${expectedRoles.join(', ')}`);
      
      return this.router.createUrlTree(['/']); 
    }
  }
}