package com.attendance.management.controller;


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
}
