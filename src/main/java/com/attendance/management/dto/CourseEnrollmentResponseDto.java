package com.attendance.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseEnrollmentResponseDto {

    private Long courseId;
    private String courseNo;
    private String courseName;
    private String courseDescription;
    private String professorName;
    private String professorUniqueId;

}
