import { Component, OnInit } from '@angular/core';
import { CourseService } from 'src/app/services/course.service';
import {
  CourseEnrollmentResponseDto,
  AttendanceResponseDto,
} from 'src/app/models/course.model'; // Import interfaces
import { AttendanceService } from 'src/app/services/attendance.service'; // Import the attendance service

@Component({
  selector: 'app-student-dashboard',
  templateUrl: './student-dashboard.component.html',
  styleUrls: ['./student-dashboard.component.css'],
})
export class StudentDashboardComponent implements OnInit {
  courses: CourseEnrollmentResponseDto[] = [];
  message: string = '';
  selectedAttendance: AttendanceResponseDto | null = null; // Holds the selected course attendance details
  attendanceMessage: string = '';
  selectedCourseNo: string | null = null; // Ensure this property is declared

  constructor(
    private courseService: CourseService,
    private attendanceService: AttendanceService
  ) {}

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

  // Method to handle clicking on a course to fetch attendance details
  onCourseClick(courseNo: string): void {
    if (this.selectedCourseNo !== courseNo) {
      // Only fetch data if a new course is selected
      this.selectedCourseNo = courseNo; // Set selected course number
      const studentUniqueId = sessionStorage.getItem('uniqueId'); // Get student unique ID from session
      if (studentUniqueId) {
        this.attendanceService
          .getAttendanceDetails(studentUniqueId, courseNo)
          .subscribe(
            (data: AttendanceResponseDto) => {
              this.selectedAttendance = data; // Set the selected attendance details
              this.attendanceMessage = data.message; // Set the attendance message
            },
            (error: any) => {
              this.selectedAttendance = null;
              this.attendanceMessage = 'Error fetching attendance details';
            }
          );
      }
    } else {
      // Reset if the same course is clicked again
      this.selectedCourseNo = null;
      this.selectedAttendance = null;
    }
  }
}
