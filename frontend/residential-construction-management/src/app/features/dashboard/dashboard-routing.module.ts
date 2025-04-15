import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../../core/guards/auth.guard';
import { RoleGuard } from '../../core/guards/role.guard';

import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { ClientDashboardComponent } from './components/client-dashboard/client-dashboard.component';
import { ContractorDashboardComponent } from './components/contractor-dashboard/contractor-dashboard.component';
import { VendorDashboardComponent } from './components/vendor-dashboard/vendor-dashboard.component';

const routes: Routes = [
  {
    path: 'admin',
    component: AdminDashboardComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { expectedRoles: ['ROLE_ADMIN'] }
  },
  {
    path: 'client',
    component: ClientDashboardComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { expectedRoles: ['ROLE_CLIENT'] }
  },
  {
    path: 'contractor',
    component: ContractorDashboardComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { expectedRoles: ['ROLE_CONTRACTOR'] }
  },
  {
    path: 'vendor',
    component: VendorDashboardComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { expectedRoles: ['ROLE_VENDOR'] }
  },
   
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule { }