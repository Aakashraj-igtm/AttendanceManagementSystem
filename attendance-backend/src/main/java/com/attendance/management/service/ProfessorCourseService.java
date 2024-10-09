package com.attendance.management.service;

import com.attendance.management.entity.Course;
import com.attendance.management.entity.ProfessorCourse;
import com.attendance.management.entity.User;
import com.attendance.management.repo.CourseRepository;
import com.attendance.management.repo.ProfessorCourseRepository;
import com.attendance.management.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfessorCourseService {

    @Autowired
    private ProfessorCourseRepository professorCourseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;


    // Assign a professor to a course and initialize noOfSeats and availableSeats
    public ProfessorCourse assignProfessorToCourse(String professorUniqueId, String courseNo, int noOfSeats, double minAttendancePercentage) {
        // Find the course by courseNo
        Optional<Course> courseOpt = courseRepository.findByCourseNo(courseNo);
        if (courseOpt.isEmpty()) {
            throw new RuntimeException("Course not found with courseNo: " + courseNo);
        }

        // Find the professor by uniqueId
        Optional<User> professorOpt = userRepository.findByUniqueId(professorUniqueId);
        if (professorOpt.isEmpty() || !"Professor".equals(professorOpt.get().getRole().getName())) {
            throw new RuntimeException("Professor not found with unique ID: " + professorUniqueId);
        }

        // Create the professor-course relationship and initialize fields
        ProfessorCourse professorCourse = new ProfessorCourse();
        professorCourse.setProfessor(professorOpt.get());
        professorCourse.setCourse(courseOpt.get());
        professorCourse.setNoOfSeats(noOfSeats);
        professorCourse.setAvailableSeats(noOfSeats);  // Initialize availableSeats to noOfSeats
        professorCourse.setMinAttendancePercentage(minAttendancePercentage);

        return professorCourseRepository.save(professorCourse);
    }
}

