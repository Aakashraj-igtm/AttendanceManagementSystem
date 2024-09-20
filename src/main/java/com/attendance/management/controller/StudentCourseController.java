package com.attendance.management.controller;

import com.attendance.management.dto.StudentResponseDto;
import com.attendance.management.entity.StudentCourse;
import com.attendance.management.entity.User;
import com.attendance.management.service.StudentCourseService;
import com.attendance.management.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/student-courses")
public class StudentCourseController {

    @Autowired
    private StudentCourseService studentCourseService;

    @Autowired
    private JwtUtil jwtUtil;

    // Endpoint to enroll a student in a course
    @PostMapping("/enroll")
    public ResponseEntity<?> enrollStudentInCourse(HttpServletRequest request,
                                                   @RequestParam String studentUniqueId,
                                                   @RequestParam String courseNo,
                                                   @RequestParam String professorUniqueId) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);  // Remove "Bearer " from the token
        String role = jwtUtil.extractRole(token);

        // Only Students are allowed to enroll in courses
        if (!"Student".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: You do not have permission to enroll in courses.");
        }

        try {
            StudentCourse studentCourse = studentCourseService.enrollStudentInCourse(studentUniqueId, courseNo, professorUniqueId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Student enrolled in course successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/students")
    public ResponseEntity<?> getStudentsByCourseAndProfessor(HttpServletRequest request,
                                                             @RequestParam String courseNo,
                                                             @RequestParam String professorUniqueId) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);  // Remove "Bearer " from the token
        String role = jwtUtil.extractRole(token);

        // Only Admins and Professors are allowed to access this API
        if (!"Admin".equals(role) && !"Professor".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: You do not have permission to access this resource");
        }

        try {
            List<StudentResponseDto> students = studentCourseService.getStudentsByCourseAndProfessor(courseNo, professorUniqueId);
            if (students.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No students found for this course and professor");
            }
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }


}
