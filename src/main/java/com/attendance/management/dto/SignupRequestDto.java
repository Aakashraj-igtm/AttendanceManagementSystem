package com.attendance.management.dto;


import lombok.Data;

@Data
public class SignupRequestDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String username;
    private String password;
    private String email;
    private String role;  // Should be one of: "Admin", "Professor", "Student"
}
