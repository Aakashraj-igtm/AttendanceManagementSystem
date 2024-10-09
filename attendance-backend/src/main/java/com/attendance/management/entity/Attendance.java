package com.attendance.management.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private User professor;  // Reference to the professor who marked attendance

    @Column(nullable = false)
    private LocalDate attendanceDate;  // Date of the class

    @Column(nullable = false)
    private boolean status;  // true for present, false for absent
}
