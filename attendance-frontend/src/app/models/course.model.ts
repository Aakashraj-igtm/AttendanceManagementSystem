// Define the interface for the course enrollment response
export interface CourseEnrollmentResponseDto {
  courseId: number;
  courseNo: string;
  courseName: string;
  courseDescription: string;
  professorName: string;
  professorUniqueId: string;
}

export interface AttendanceResponseDto {
  attendancePercentage: number;
  minAttendancePercentage: number;
  message: string;
}
