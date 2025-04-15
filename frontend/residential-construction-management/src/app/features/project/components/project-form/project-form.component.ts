import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProjectService, ProjectRequestPayload } from '../../services/project.service'; 
import { switchMap } from 'rxjs/operators';
import { of } from 'rxjs'; 

@Component({
  selector: 'app-project-form',
  templateUrl: './project-form.component.html',
  styleUrls: ['./project-form.component.scss']
})
export class ProjectFormComponent implements OnInit {
  projectForm: FormGroup;
  isEditMode = false;
  projectId: number | null = null;
  isLoading = false;
  errorMessage = '';
  submitted = false;

  
  projectStatuses: string[] = ['Planning', 'In Progress', 'On Hold', 'Completed', 'Cancelled'];
  projectTypes: string[] = ['New Build', 'Renovation', 'Extension', 'Maintenance', 'Other'];


  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private projectService: ProjectService
  ) {
    this.projectForm = this.fb.group({
      projectName: ['', [Validators.required, Validators.maxLength(100)]],
      description: [''],
      projectType: ['', [Validators.maxLength(50)]],
      status: ['', [Validators.maxLength(30)]],
      startDate: [null], 
      endDate: [null]
    });
  }

  ngOnInit(): void {
    this.route.paramMap.pipe(
      switchMap(params => {
        const id = params.get('id');
        if (id) {
          this.isEditMode = true;
          this.projectId = +id; // Convert string id to number
          this.isLoading = true;
          return this.projectService.getProjectById(this.projectId);
        } else {
          this.isEditMode = false;
          return of(null); // Return observable of null if creating new
        }
      })
    ).subscribe({
        next: (project) => {
            if (project && this.isEditMode) {
                // Format dates for the form control (YYYY-MM-DD)
                const formattedStartDate = project.startDate ? new Date(project.startDate).toISOString().split('T')[0] : null;
                const formattedEndDate = project.endDate ? new Date(project.endDate).toISOString().split('T')[0] : null;

                this.projectForm.patchValue({
                    ...project,
                    startDate: formattedStartDate,
                    endDate: formattedEndDate
                 });
            }
            this.isLoading = false;
        },
        error: (err) => {
            console.error('Error loading project for edit:', err);
            this.errorMessage = 'Failed to load project data.';
            this.isLoading = false;
        }
    });
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.projectForm.invalid) {
        this.errorMessage = 'Please correct the errors in the form.';
        this.projectForm.markAllAsTouched(); // Mark fields to show errors
        return;
    }

    if (this.isLoading) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = ''; // Clear previous errors

    const projectData: ProjectRequestPayload = this.projectForm.value;

    // Ensure dates are handled correctly 
    projectData.startDate = projectData.startDate || null;
    projectData.endDate = projectData.endDate || null;


    const saveObservable = this.isEditMode && this.projectId
      ? this.projectService.updateProject(this.projectId, projectData)
      : this.projectService.createProject(projectData);

    saveObservable.subscribe({
      next: () => {
        this.isLoading = false;
        console.log('Project saved successfully');
        this.router.navigate(['/projects']); // Navigate back to the list
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Failed to save project. Please try again.';
        console.error('Error saving project:', err);
      }
    });
  }

  // Getters for template validation
  get projectName() { return this.projectForm.get('projectName'); }
  get projectType() { return this.projectForm.get('projectType'); }
  get status() { return this.projectForm.get('status'); }
}