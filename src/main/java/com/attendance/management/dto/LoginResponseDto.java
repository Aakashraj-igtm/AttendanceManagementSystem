package com.attendance.management.dto;



import lombok.Data;

@Data
public class LoginResponseDto {
    private String jwtToken;

    public LoginResponseDto(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
