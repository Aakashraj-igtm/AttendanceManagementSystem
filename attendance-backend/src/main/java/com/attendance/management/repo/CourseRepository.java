package com.attendance.management.repo;


import com.attendance.management.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCourseNo(String courseNo);
    boolean existsByCourseNo(String courseNo);
}
