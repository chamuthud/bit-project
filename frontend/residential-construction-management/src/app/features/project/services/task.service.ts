import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


const API_BASE_URL = 'http://localhost:8080/api/projects';


export interface Task {
    taskId: number;
    taskName: string;
    description?: string;
    priority?: number;
    taskStatus?: string;
    startDate?: string;
    endDate?: string;
    projectId: number;
    assignedToUserId?: number;
    assignedToUsername?: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface TaskRequestPayload {
    taskName: string;
    description?: string;
    priority?: number | null;
    taskStatus?: string;
    startDate?: string | null;
    endDate?: string | null;
    assignedToUserId?: number | null;
}


@Injectable({
  providedIn: 'root' 
})
export class TaskService {

  constructor(private http: HttpClient) { }

  // Get all tasks for a specific project
  getTasksForProject(projectId: number): Observable<Task[]> {
    return this.http.get<Task[]>(`${API_BASE_URL}${projectId}/tasks`);
  }

  // Get a single task within a project
  getTaskById(projectId: number, taskId: number): Observable<Task> {
    return this.http.get<Task>(`${API_BASE_URL}${projectId}/tasks/${taskId}`);
  }

  // Create a task within a project
  createTask(projectId: number, taskData: TaskRequestPayload): Observable<Task> {
    return this.http.post<Task>(`${API_BASE_URL}${projectId}/tasks`, taskData);
  }

  // Update a task within a project
  updateTask(projectId: number, taskId: number, taskData: TaskRequestPayload): Observable<Task> {
    return this.http.put<Task>(`${API_BASE_URL}${projectId}/tasks/${taskId}`, taskData);
  }

  // Delete a task within a project
  deleteTask(projectId: number, taskId: number): Observable<any> {
    return this.http.delete(`${API_BASE_URL}${projectId}/tasks/${taskId}`);
  }
}