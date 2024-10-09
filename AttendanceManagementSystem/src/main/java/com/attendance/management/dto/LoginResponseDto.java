package com.attendance.management.dto;

public class LoginResponseDto {
    private String token;
    private String role;
    private String uniqueId; // Add this property

    // Constructors
    public LoginResponseDto(String token, String role, String uniqueId) {
        this.token = token;
        this.role = role;
        this.uniqueId = uniqueId;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
