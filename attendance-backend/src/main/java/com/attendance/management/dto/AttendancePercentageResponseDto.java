package com.attendance.management.dto;
public class AttendancePercentageResponseDto {

    private double attendancePercentage;
    private double minAttendancePercentage;  // Include minimum attendance percentage
    private String message;

    // Getters and setters
    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public double getMinAttendancePercentage() {
        return minAttendancePercentage;
    }

    public void setMinAttendancePercentage(double minAttendancePercentage) {
        this.minAttendancePercentage = minAttendancePercentage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
