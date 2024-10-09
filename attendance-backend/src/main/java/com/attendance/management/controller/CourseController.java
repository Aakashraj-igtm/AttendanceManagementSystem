package com.attendance.management.controller;

import com.attendance.management.entity.Course;
import com.attendance.management.service.CourseService;
import com.attendance.management.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private JwtUtil jwtUtil;

    // Endpoint for admin to add a course
    @PostMapping("/add")
    public ResponseEntity<String> addCourse(HttpServletRequest request, @RequestBody Course course) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);  // Remove "Bearer " from the token
        String role = jwtUtil.extractRole(token);

        if (!"Admin".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: You do not have permission to add courses");
        }

        try {
            Course savedCourse = courseService.addCourse(course);
            return ResponseEntity.status(HttpStatus.CREATED).body("Course added successfully: " + savedCourse.getCourseName());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    // Endpoint for admin to update a course by courseNo
    @PutMapping("/update/{courseNo}")
    public ResponseEntity<String> updateCourse(HttpServletRequest request, @PathVariable String courseNo, @RequestBody Course course) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);  // Remove "Bearer " from the token
        String role = jwtUtil.extractRole(token);

        if (!"Admin".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: You do not have permission to update courses");
        }

        try {
            Course updatedCourse = courseService.updateCourseByCourseNo(courseNo, course);
            return ResponseEntity.ok("Course updated successfully: " + updatedCourse.getCourseName());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}
