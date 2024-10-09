import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  userDetails = {
    username: '',
    password: '',
    firstName: '',
    lastName: '',
    phoneNumber: '',
    email: '',
    role: ''
  };
  successMessage: string = '';
  errorMessage: string = '';

  ngOnInit(): void {
    
  }

  constructor(private authService: AuthService, private router: Router) {}

  onSignup() {
    this.authService.signup(this.userDetails).subscribe(
      (response) => {
        console.log(response);
        this.successMessage = response.message;
        // Redirect to login page after signup

        this.router.navigate(['/login']);
      },
      (error) => {
        console.log("error is" + error.message);
        this.errorMessage = error.error || 'Failed to register user';
      }
    );
  }

}
