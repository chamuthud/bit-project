import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core'; 
import { Task, TaskService } from '../../services/task.service';
import { Observable } from 'rxjs';
import { Router } from '@angular/router'; 

@Component({
  selector: 'app-task-list', 
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss']
})

export class TaskListComponent implements OnInit, OnChanges {

  
  @Input() projectId: number | null = null;

  tasks$: Observable<Task[]> | undefined;
  isLoading = false;
  errorMessage = '';


  taskStatuses: string[] = ['To Do', 'In Progress', 'Done', 'Blocked'];


  constructor(private taskService: TaskService, private router: Router) { }

  ngOnInit(): void {
    this.loadTasks();
  }

  
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['projectId'] && !changes['projectId'].firstChange) {
      this.loadTasks(); // Reload tasks if projectId changes
    }
  }

  loadTasks(): void {
    if (this.projectId) {
      this.isLoading = true;
      this.errorMessage = '';
      this.tasks$ = this.taskService.getTasksForProject(this.projectId);
      this.tasks$.subscribe({
         next: () => this.isLoading = false, // Stop loading on success
         error: (err) => {
            this.errorMessage = err.message || 'Failed to load tasks.';
            this.isLoading = false;
            console.error("Error loading tasks:", err);
         }
      });
    } else {
      this.tasks$ = undefined; // Clear tasks if no projectId
      this.errorMessage = 'Project ID is missing.';
    }
  }

  deleteTask(taskId: number): void {
    if (!this.projectId) {
        alert('Cannot delete task: Project ID is missing.');
        return;
    }
    if (confirm('Are you sure you want to delete this task?')) {
      this.taskService.deleteTask(this.projectId, taskId).subscribe({
        next: () => {
          console.log('Task deleted successfully');
          this.loadTasks(); // Refresh the list
        },
        error: (err) => {
          console.error('Error deleting task:', err);
          alert(err.message || 'Failed to delete task.'); // Show error
        }
      });
    }
  }

  // Navigate to edit task form
  editTask(taskId: number): void {
     if (this.projectId) {
       this.router.navigate(['/projects', this.projectId, 'tasks', 'edit', taskId]);
     }
  }

   // Navigate to add task form
  addTask(): void {
    if (this.projectId) {
       this.router.navigate(['/projects', this.projectId, 'tasks', 'new']);
    }
  }
}