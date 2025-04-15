import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TaskService, TaskRequestPayload } from '../../services/task.service';
import { ProjectService } from '../../services/project.service'; 
import { switchMap, catchError } from 'rxjs/operators';
import { of, throwError } from 'rxjs'; 



@Component({
  selector: 'app-task-form',
  templateUrl: './task-form.component.html',
  styleUrls: ['./task-form.component.scss']
})
export class TaskFormComponent implements OnInit {
  taskForm: FormGroup;
  isEditMode = false;
  projectId: number | null = null;
  taskId: number | null = null;
  isLoading = false;
  errorMessage = '';
  pageTitle = 'Create Task'; 
  submitted = false;

 
  taskStatuses: string[] = ['To Do', 'In Progress', 'Done', 'Blocked'];
  taskPriorities: { value: number, label: string }[] = [
    { value: 1, label: 'High' },
    { value: 2, label: 'Medium' },
    { value: 3, label: 'Low' }
  ];


  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private taskService: TaskService,
    private projectService: ProjectService 
    
  ) {
    this.taskForm = this.fb.group({
      taskName: ['', [Validators.required, Validators.maxLength(150)]],
      description: [''],
      priority: [null], 
      taskStatus: ['To Do', [Validators.required]], 
      startDate: [null],
      endDate: [null],
      assignedToUserId: [null] 
    });
  }

  ngOnInit(): void {
    this.isLoading = true; 

   
    this.route.paramMap.pipe(
      switchMap(params => {
        const pId = params.get('projectId');
        const tId = params.get('taskId'); 

        if (!pId) {
          this.errorMessage = "Project ID is missing from the route.";
          console.error(this.errorMessage);
          this.isLoading = false;
          return throwError(() => new Error(this.errorMessage)); 
        }

        this.projectId = +pId;
        this.taskId = tId ? +tId : null;
        this.isEditMode = !!this.taskId; 
        this.pageTitle = this.isEditMode ? 'Edit Task' : 'Create Task';


        if (this.isEditMode && this.taskId) {
          return this.taskService.getTaskById(this.projectId, this.taskId);
        } else {
          this.isLoading = false; 
          return of(null); 
        }
      }),
      catchError(err => {
         console.error("Error retrieving data:", err);
         this.errorMessage = err.message || 'Failed to load data for task form.';
         this.isLoading = false;
         return throwError(() => err); 
      })
    ).subscribe(task => {
      if (task && this.isEditMode) {
        const formattedStartDate = task.startDate ? new Date(task.startDate).toISOString().split('T')[0] : null;
        const formattedEndDate = task.endDate ? new Date(task.endDate).toISOString().split('T')[0] : null;

        this.taskForm.patchValue({
            ...task,
            startDate: formattedStartDate,
            endDate: formattedEndDate,
            assignedToUserId: task.assignedToUserId || null
         });
      }
      this.isLoading = false; 
    });

    
  }

  onSubmit(): void {
    this.submitted = true;
    
    if (this.taskForm.invalid) {
        this.errorMessage = 'Please correct the errors in the form.';
        this.taskForm.markAllAsTouched();
        return;
    }
    if (!this.projectId) {
        this.errorMessage = 'Project ID is missing. Cannot save task.';
        return;
    }

    if (this.isLoading) {
      return;
    }


    this.isLoading = true;
    this.errorMessage = '';

    const taskData: TaskRequestPayload = {
        ...this.taskForm.value,
        priority: this.taskForm.value.priority ? +this.taskForm.value.priority : null, 
        assignedToUserId: this.taskForm.value.assignedToUserId || null, 
        startDate: this.taskForm.value.startDate || null,
        endDate: this.taskForm.value.endDate || null
    };

    const saveObservable = this.isEditMode && this.taskId
      ? this.taskService.updateTask(this.projectId, this.taskId, taskData)
      : this.taskService.createTask(this.projectId, taskData);

    saveObservable.subscribe({
      next: () => {
        this.isLoading = false;
        console.log('Task saved successfully');
       
        this.router.navigate(['/projects']); 
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.message || 'Failed to save task.';
        console.error('Error saving task:', err);
      }
    });
  }

  // Cancel navigation
  cancel(): void {
    if (this.projectId && !this.isLoading) { 
      this.router.navigate(['/projects', this.projectId]); 
  } else if (!this.isLoading) {
      this.router.navigate(['/projects']); 
  }
  }

  // --- Getters for template validation ---
  get taskName() { return this.taskForm.get('taskName'); }
  get taskStatus() { return this.taskForm.get('taskStatus'); }
 
}