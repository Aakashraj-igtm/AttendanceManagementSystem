import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CourseEnrollmentResponseDto } from '../models/course.model';
@Injectable({
  providedIn: 'root'
})
export class CourseService {
  private apiUrl = 'http://localhost:8080/api/student-courses'; // Base URL for courses

  constructor(private http: HttpClient) { }

   // Fetch enrolled courses for a student by their unique ID
   getStudentCourses(studentUniqueId: string): Observable<CourseEnrollmentResponseDto[]> {
    const url = `${this.apiUrl}/enrolled-courses?studentUniqueId=${studentUniqueId}`;

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('authToken')}`
    });
    return this.http.get<CourseEnrollmentResponseDto[]>(url, { headers });
  }
}
