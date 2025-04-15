import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProjectListComponent } from './components/project-list/project-list.component';
import { ProjectFormComponent } from './components/project-form/project-form.component';
import { TaskListComponent } from './components/task-list/task-list.component';
import { TaskFormComponent } from './components/task-form/task-form.component';
import { ProjectDetailComponent } from './components/project-detail/project-detail.component';

const routes: Routes = [
  { path: '', component: ProjectListComponent },              
  { path: 'new', component: ProjectFormComponent },            
  { path: ':id', component: ProjectDetailComponent },          
  { path: 'edit/:id', component: ProjectFormComponent },       
  { path: ':projectId/tasks/new', component: TaskFormComponent }, 
  { path: ':projectId/tasks/edit/:taskId', component: TaskFormComponent }, 
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProjectRoutingModule { }
