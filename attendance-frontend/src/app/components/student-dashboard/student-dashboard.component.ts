import { Component, OnInit } from '@angular/core';
import { CourseService } from 'src/app/services/course.service';
import { CourseEnrollmentResponseDto } from 'src/app/models/course.model';// Import the interface

@Component({
  selector: 'app-student-dashboard',
  templateUrl: './student-dashboard.component.html',
  styleUrls: ['./student-dashboard.component.css']
})
export class StudentDashboardComponent implements OnInit {
  courses: CourseEnrollmentResponseDto[] = [];
  message: string = '';

  constructor(private courseService: CourseService) {}

  ngOnInit(): void {
    const uniqueId = sessionStorage.getItem('uniqueId');
    if (uniqueId) {
      this.courseService.getStudentCourses(uniqueId).subscribe(
        (data: CourseEnrollmentResponseDto[]) => {
          if (data && data.length > 0) {
            this.courses = data;
          } else {
            this.message = 'No courses found for the given student.';
          }
        },
        (error: any) => {
          this.message = error.error || 'Error fetching courses';
        }
      );
    }
  }
}
