package com.attendance.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
@Entity

@Data
@Table(name = "\"user\"")  // Escape the reserved word
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String firstName;  // First Name of the User

    @Column(nullable = false)
    private String lastName;  // Last Name of the User

    @Column(nullable = false, unique = true, length = 10)
    private String phoneNumber;  // 10-digit phone number

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(nullable = false, unique = true, length = 8)
    private String uniqueId;  // 8-digit unique identifier

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
