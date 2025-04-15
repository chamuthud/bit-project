import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms'; 

import { ProjectRoutingModule } from './project-routing.module';
import { ProjectListComponent } from './components/project-list/project-list.component';
import { ProjectFormComponent } from './components/project-form/project-form.component';
import { TaskListComponent } from './components/task-list/task-list.component';
import { TaskFormComponent } from './components/task-form/task-form.component';
import { ProjectDetailComponent } from './components/project-detail/project-detail.component';




@NgModule({
  declarations: [
    ProjectListComponent,
    ProjectFormComponent,
    TaskListComponent,
    TaskFormComponent,
    ProjectDetailComponent
  ],
  imports: [
    CommonModule,
    ProjectRoutingModule,
    ReactiveFormsModule 
  ]
 
})
export class ProjectModule { }