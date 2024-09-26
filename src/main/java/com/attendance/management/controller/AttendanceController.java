package com.attendance.management.controller;


import com.attendance.management.dto.AttendancePercentageResponseDto;
import com.attendance.management.dto.AttendanceRequestDto;
import com.attendance.management.service.AttendanceService;
import com.attendance.management.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private JwtUtil jwtUtil;

    // API to mark attendance
    @PostMapping("/mark")
    public ResponseEntity<?> markAttendance(HttpServletRequest request, @RequestBody AttendanceRequestDto attendanceRequestDto) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);  // Remove "Bearer " from the token
        String role = jwtUtil.extractRole(token);

        // Only Professors are allowed to mark attendance
        if (!"Professor".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: You do not have permission to mark attendance.");
        }

        try {
            attendanceService.markAttendance(
                    attendanceRequestDto.getProfessorUniqueId(),
                    attendanceRequestDto.getCourseNo(),
                    attendanceRequestDto.getAttendanceDate(),
                    attendanceRequestDto.getPresentStudentUniqueIds()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body("Attendance marked successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    // API to update attendance
    @PostMapping("/update")
    public ResponseEntity<?> updateAttendance(HttpServletRequest request, @RequestBody AttendanceRequestDto attendanceRequestDto) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);  // Remove "Bearer " from the token
        String role = jwtUtil.extractRole(token);

        // Only Professors are allowed to update attendance
        if (!"Professor".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: You do not have permission to update attendance.");
        }

        try {
            attendanceService.updateAttendance(
                    attendanceRequestDto.getProfessorUniqueId(),
                    attendanceRequestDto.getCourseNo(),
                    attendanceRequestDto.getAttendanceDate(),
                    attendanceRequestDto.getPresentStudentUniqueIds()
            );
            return ResponseEntity.status(HttpStatus.OK).body("Attendance updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/calculate-percentage")
    public ResponseEntity<?> calculateAttendance(HttpServletRequest request, @RequestParam String studentUniqueId, @RequestParam String courseNo) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Missing or invalid JWT token.");
        }

        // Extract token and validate
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        String role = jwtUtil.extractRole(token); // Extract role from the token
        String loggedInStudentUniqueId = jwtUtil.extractUsername(token); // Extract the studentUniqueId from the token

        // Ensure the logged-in user is a student
        if (!"Student".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: You do not have permission to access this resource.");
        }

        try {
            // Calculate attendance percentage
            AttendancePercentageResponseDto response = attendanceService.calculateAttendance(studentUniqueId, courseNo);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

}
