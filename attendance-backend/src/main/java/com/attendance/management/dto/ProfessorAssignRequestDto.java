package com.attendance.management.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class ProfessorAssignRequestDto {

    String professorUniqueId;
    String courseNo;
    int noOfSeats;
    double minAttendancePercentage;
}
