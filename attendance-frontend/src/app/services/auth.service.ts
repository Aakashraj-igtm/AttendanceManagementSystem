import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  // Login Method
  login(username: string, password: string): Observable<any> {
    const url = `${this.apiUrl}/login`;
    return this.http.post(url, { username, password });
  }

  // Signup Method
  signup(userDetails: any): Observable<any> {
    const url = `${this.apiUrl}/signup`;
    console.log(userDetails);
    return this.http.post(url, userDetails);
  }
}
