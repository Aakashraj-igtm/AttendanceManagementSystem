package com.attendance.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(nullable = false, unique = true)
    private String courseName;

    @Column
    private String description;

    @Column(nullable = false, unique = true)
    private String courseNo;  // New course_no field

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
