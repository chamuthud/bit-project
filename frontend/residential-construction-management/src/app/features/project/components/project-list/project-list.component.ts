import { Component, OnInit } from '@angular/core';
import { Project, ProjectService } from '../../services/project.service'; 
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';



@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.scss']
})
export class ProjectListComponent implements OnInit {

  projects$: Observable<Project[]> | undefined; 
  isLoading = false;
  errorMessage = '';

  constructor(private projectService: ProjectService) { }

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects(): void {
    this.isLoading = true; // Start loading
    this.errorMessage = ''; // Clear error
    this.projects$ = this.projectService.getProjects().pipe(
      catchError((err: any ) => {
          console.error("Error loading projects:", err);
          this.errorMessage = err.message || 'Failed to load projects.';
          this.isLoading = false; // Stop loading on error
          return of([]); // Return empty array on error to avoid breaking async pipe
        })
    );
    
    this.projects$.subscribe(() => this.isLoading = false);
  }

  deleteProject(id: number): void {
     
     if (confirm('Are you sure you want to delete this project?')) {
      this.projectService.deleteProject(id).subscribe({
        next: () => {
          console.log('Project deleted successfully');
          this.loadProjects(); // Refresh
        },
        error: (err) => {
          console.error('Error deleting project:', err);
          alert(err.message || 'Failed to delete project.');
        }
      });
    }
  }
}