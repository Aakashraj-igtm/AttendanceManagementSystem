import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin() {
    this.authService.login(this.username, this.password).subscribe(
      (response) => {
        // Save the JWT token in localStorage/sessionStorage
        console.log(response.token)
        localStorage.setItem('authToken', response.token);
        localStorage.setItem('uniqueId', response.uniqueId);
        localStorage.setItem('userRole', response.role);
        // Redirect to a secure route (e.g., dashboard)
        if(response.role == 'Student')
          console.log('hi')
          this.router.navigate(['/student-dashboard']);
      },
      (error) => {
        this.errorMessage = error.error || 'Invalid credentials';
      }
    );
  }

  ngOnInit(): void {
  }

}
