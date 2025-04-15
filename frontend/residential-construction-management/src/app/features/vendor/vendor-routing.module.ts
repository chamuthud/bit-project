import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { VendorItemListComponent } from './components/vendor-item-list/vendor-item-list.component';
import { VendorItemFormComponent } from './components/vendor-item-form/vendor-item-form.component';

const routes: Routes = [
  
  
  // Item Management routes
  { path: 'items', component: VendorItemListComponent },       
  { path: 'items/new', component: VendorItemFormComponent },    
  { path: 'items/edit/:itemId', component: VendorItemFormComponent }, 

  
   { path: '', redirectTo: 'items', pathMatch: 'full' } 
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class VendorRoutingModule { }