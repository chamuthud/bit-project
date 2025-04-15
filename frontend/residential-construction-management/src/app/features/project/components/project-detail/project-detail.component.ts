import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Project, ProjectService } from '../../services/project.service'; 
import { Observable, of } from 'rxjs'; 
import { catchError } from 'rxjs/operators'; 

@Component({
  selector: 'app-project-detail',
  templateUrl: './project-detail.component.html',
  styleUrls: ['./project-detail.component.scss']
})
export class ProjectDetailComponent implements OnInit {

  project$: Observable<Project | null> | undefined; 
  projectId: number | null = null;
  isLoading = true;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router, 
    private projectService: ProjectService
  ) { }

  ngOnInit(): void {
    this.isLoading = true;
    this.errorMessage = '';

    // Get project ID from route parameters
    const idParam = this.route.snapshot.paramMap.get('id'); // Use snapshot for initial load

    if (idParam) {
      this.projectId = +idParam; // Convert string ID to number
      this.project$ = this.projectService.getProjectById(this.projectId).pipe(
        catchError(err => {
          console.error("Error loading project details:", err);
          this.errorMessage = err.message || `Failed to load project with ID ${this.projectId}.`;
          this.isLoading = false;
          return of(null); 
        })
      );
       // Stop loading indicator once data arrives (or error occurs)
      this.project$.subscribe(() => this.isLoading = false);

    } else {
      // Handle case where ID is missing or invalid
      this.errorMessage = 'Invalid Project ID provided in the URL.';
      this.isLoading = false;
      this.project$ = of(null); 
      console.error(this.errorMessage);
    }
  }

 
  navigateToEdit(): void {
    if (this.projectId) {
      this.router.navigate(['/projects/edit', this.projectId]);
    }
  }


}
