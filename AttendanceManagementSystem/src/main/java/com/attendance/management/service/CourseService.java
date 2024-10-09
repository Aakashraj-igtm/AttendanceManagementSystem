package com.attendance.management.service;

import com.attendance.management.entity.Course;
import com.attendance.management.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    // Add a course
    public Course addCourse(Course course) {
        if (courseRepository.existsByCourseNo(course.getCourseNo())) {
            throw new RuntimeException("Course with the same course number already exists");
        }
        return courseRepository.save(course);
    }

    // Update a course by courseNo
    public Course updateCourseByCourseNo(String courseNo, Course updatedCourse) {
        Optional<Course> existingCourseOpt = courseRepository.findByCourseNo(courseNo);

        if (existingCourseOpt.isPresent()) {
            Course existingCourse = existingCourseOpt.get();
            existingCourse.setCourseName(updatedCourse.getCourseName());
            existingCourse.setDescription(updatedCourse.getDescription());
            return courseRepository.save(existingCourse);
        } else {
            throw new RuntimeException("Course not found with course number: " + courseNo);
        }
    }
}
