package com.attendance.management.service;

import com.attendance.management.dto.StudentResponseDto;
import com.attendance.management.entity.Course;
import com.attendance.management.entity.ProfessorCourse;
import com.attendance.management.entity.StudentCourse;
import com.attendance.management.entity.User;
import com.attendance.management.repo.CourseRepository;
import com.attendance.management.repo.ProfessorCourseRepository;
import com.attendance.management.repo.StudentCourseRepository;
import com.attendance.management.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentCourseService {

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProfessorCourseRepository professorCourseRepository;

    // Enroll a student in a course by uniqueId and check availableSeats
    public StudentCourse enrollStudentInCourse(String studentUniqueId, String courseNo, String professorUniqueId) {
        // Find the course by courseNo
        Optional<Course> courseOpt = courseRepository.findByCourseNo(courseNo);
        if (courseOpt.isEmpty()) {
            throw new RuntimeException("Course not found with courseNo: " + courseNo);
        }

        // Find the student by uniqueId
        Optional<User> studentOpt = userRepository.findByUniqueId(studentUniqueId);
        if (studentOpt.isEmpty() || !"Student".equals(studentOpt.get().getRole().getName())) {
            throw new RuntimeException("Student not found with unique ID: " + studentUniqueId);
        }

        // Find the professor-course relationship to check availableSeats
        Optional<ProfessorCourse> professorCourseOpt = professorCourseRepository.findByProfessorAndCourse(professorUniqueId, courseOpt.get().getCourseId());
        if (professorCourseOpt.isEmpty()) {
            throw new RuntimeException("Professor is not assigned to this course.");
        }

        ProfessorCourse professorCourse = professorCourseOpt.get();

        // Check if there are available seats
        if (professorCourse.getAvailableSeats() <= 0) {
            throw new RuntimeException("No available seats for this course.");
        }

        // Reduce availableSeats by 1
        professorCourse.setAvailableSeats(professorCourse.getAvailableSeats() - 1);
        professorCourseRepository.save(professorCourse);

        // Create the student-course relationship
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudent(studentOpt.get());
        studentCourse.setCourse(courseOpt.get());

        return studentCourseRepository.save(studentCourse);
    }

    // Fetch students enrolled in a course by courseNo and professorUniqueId
    public List<StudentResponseDto> getStudentsByCourseAndProfessor(String courseNo, String professorUniqueId) {
        return studentCourseRepository.findStudentsByCourseNoAndProfessorUniqueId(courseNo, professorUniqueId);
    }
}
