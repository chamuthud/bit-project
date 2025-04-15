import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

import { VendorRoutingModule } from './vendor-routing.module';
import { VendorComponent } from './vendor.component';
import { VendorItemListComponent } from './components/vendor-item-list/vendor-item-list.component';
import { VendorItemFormComponent } from './components/vendor-item-form/vendor-item-form.component';


@NgModule({
  declarations: [
    VendorComponent,
    VendorItemListComponent,
    VendorItemFormComponent
  ],
  imports: [
    CommonModule,
    VendorRoutingModule,
    ReactiveFormsModule
  ]
})
export class VendorModule { }
