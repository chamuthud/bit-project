import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


export interface Project {
  projectId: number;
  projectName: string;
  description?: string; 
  projectType?: string;
  status?: string;
  startDate?: string; 
  endDate?: string;
  createdByUserId?: number;
  createdByUsername?: string;
  createdAt?: string;
  updatedAt?: string;
}


export interface ProjectRequestPayload {
    projectName: string;
    description?: string;
    projectType?: string;
    status?: string;
    startDate?: string | null;
    endDate?: string | null;
}


const API_URL = 'http://localhost:8080/api/projects'; 

@Injectable({
  providedIn: 'root' 
})
export class ProjectService {

  constructor(private http: HttpClient) { }

 
  getProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(API_URL);
  }

  // Get a single project by ID
  getProjectById(id: number): Observable<Project> {
    return this.http.get<Project>(API_URL + id);
  }

  // Create a new project
  createProject(projectData: ProjectRequestPayload): Observable<Project> {
    return this.http.post<Project>(API_URL, projectData);
  }

  // Update an existing project
  updateProject(id: number, projectData: ProjectRequestPayload): Observable<Project> {
    return this.http.put<Project>(API_URL + id, projectData);
  }

  // Delete a project
  deleteProject(id: number): Observable<any> { // Expecting no content (200 OK) or potentially an error
    return this.http.delete(API_URL + id);
  }
}