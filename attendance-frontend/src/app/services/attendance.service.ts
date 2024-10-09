import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AttendanceResponseDto } from 'src/app/models/course.model'; // Import the AttendanceResponseDto interface

@Injectable({
  providedIn: 'root',
})
export class AttendanceService {
  private baseUrl = 'http://localhost:8080/api/attendance';

  constructor(private http: HttpClient) {}

  getAttendanceDetails(
    studentUniqueId: string,
    courseNo: string
  ): Observable<AttendanceResponseDto> {
    const url = `${this.baseUrl}/calculate-percentage?studentUniqueId=${studentUniqueId}&courseNo=${courseNo}`;

    // Fetch JWT token from local storage
    const authToken = localStorage.getItem('authToken');

    // Set headers with the Authorization Bearer token
    const headers = new HttpHeaders({
      Authorization: `Bearer ${authToken}`,
    });

    // Pass the headers along with the GET request
    return this.http.get<AttendanceResponseDto>(url, { headers });
  }
}
