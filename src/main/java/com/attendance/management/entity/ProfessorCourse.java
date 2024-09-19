package com.attendance.management.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProfessorCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private User professor;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
