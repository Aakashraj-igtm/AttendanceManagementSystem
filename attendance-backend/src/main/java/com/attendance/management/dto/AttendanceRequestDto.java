package com.attendance.management.dto;

import java.time.LocalDate;
import java.util.List;

public class AttendanceRequestDto {

    private String courseNo;
    private String professorUniqueId;
    private LocalDate attendanceDate;
    private List<String> presentStudentUniqueIds;

    // Getters and setters
    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getProfessorUniqueId() {
        return professorUniqueId;
    }

    public void setProfessorUniqueId(String professorUniqueId) {
        this.professorUniqueId = professorUniqueId;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public List<String> getPresentStudentUniqueIds() {
        return presentStudentUniqueIds;
    }

    public void setPresentStudentUniqueIds(List<String> presentStudentUniqueIds) {
        this.presentStudentUniqueIds = presentStudentUniqueIds;
    }
}
