import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router'; 

import { DashboardRoutingModule } from './dashboard-routing.module';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { ClientDashboardComponent } from './components/client-dashboard/client-dashboard.component';
import { ContractorDashboardComponent } from './components/contractor-dashboard/contractor-dashboard.component';
import { VendorDashboardComponent } from './components/vendor-dashboard/vendor-dashboard.component';


@NgModule({
  declarations: [
    AdminDashboardComponent,
    ClientDashboardComponent,
    ContractorDashboardComponent,
    VendorDashboardComponent
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    RouterModule 
  ]
})
export class DashboardModule { }