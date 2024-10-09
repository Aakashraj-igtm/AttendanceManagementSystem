package com.attendance.management.controller;


import com.attendance.management.dto.ProfessorAssignRequestDto;
import com.attendance.management.entity.ProfessorCourse;
import com.attendance.management.service.ProfessorCourseService;
import com.attendance.management.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/professor-courses")
public class ProfessorCourseController {

    @Autowired
    private ProfessorCourseService professorCourseService;

    @Autowired
    private JwtUtil jwtUtil;

    // Endpoint to assign a professor to a course
    @PostMapping("/assign")
    public ResponseEntity<String> assignProfessorToCourse(HttpServletRequest request,
                                                          @RequestBody ProfessorAssignRequestDto professorAssignRequestDto) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);  // Remove "Bearer " from the token
        String role = jwtUtil.extractRole(token);

        // Only Admins are allowed to assign professors to courses
        if (!"Admin".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: You do not have permission to assign professors to courses");
        }

        try {
            ProfessorCourse professorCourse = professorCourseService.assignProfessorToCourse(
                    professorAssignRequestDto.getProfessorUniqueId(),
                    professorAssignRequestDto.getCourseNo(),
                    professorAssignRequestDto.getNoOfSeats(),
                    professorAssignRequestDto.getMinAttendancePercentage());

            return ResponseEntity.status(HttpStatus.CREATED).body("Professor assigned to course successfully with " +
                    professorAssignRequestDto.getNoOfSeats() + " seats.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

}
