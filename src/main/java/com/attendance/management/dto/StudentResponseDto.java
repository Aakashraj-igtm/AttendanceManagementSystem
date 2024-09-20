package com.attendance.management.dto;

public class StudentResponseDto {

    private String firstName;
    private String lastName;
    private String uniqueId;
    private String email;

    // Constructor
    public StudentResponseDto(String firstName, String lastName, String uniqueId, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.uniqueId = uniqueId;
        this.email = email;
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
