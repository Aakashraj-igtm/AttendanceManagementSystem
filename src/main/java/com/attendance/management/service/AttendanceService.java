package com.attendance.management.service;


import com.attendance.management.entity.Attendance;
import com.attendance.management.entity.Course;
import com.attendance.management.entity.ProfessorCourse;
import com.attendance.management.entity.User;
import com.attendance.management.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProfessorCourseRepository professorCourseRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    // Method to mark attendance for a list of students
    public void markAttendance(String professorUniqueId, String courseNo, LocalDate attendanceDate, List<String> presentStudentUniqueIds) {
        // Find the course by courseNo
        Optional<Course> courseOpt = courseRepository.findByCourseNo(courseNo);
        if (courseOpt.isEmpty()) {
            throw new RuntimeException("Course not found with courseNo: " + courseNo);
        }

        Course course = courseOpt.get();

        // Find the professor-course relationship
        Optional<ProfessorCourse> professorCourseOpt = professorCourseRepository.findByProfessorAndCourse(professorUniqueId, course.getCourseId());
        if (professorCourseOpt.isEmpty()) {
            throw new RuntimeException("Professor is not assigned to this course.");
        }

        Long professorId = professorCourseOpt.get().getProfessor().getUserId();

        // Get the list of students enrolled under this professor for the specific course
        List<User> enrolledStudents = studentCourseRepository.findEnrolledStudentsByProfessorAndCourse(professorId, course.getCourseId());

        // Separate the students into present and absent lists
        List<String> enrolledStudentUniqueIds = enrolledStudents.stream()
                .map(User::getUniqueId)
                .collect(Collectors.toList());

        List<String> absentStudentUniqueIds = enrolledStudentUniqueIds.stream()
                .filter(uniqueId -> !presentStudentUniqueIds.contains(uniqueId))
                .collect(Collectors.toList());

        // Mark attendance for present students
        for (String studentUniqueId : presentStudentUniqueIds) {
            Optional<User> studentOpt = userRepository.findByUniqueId(studentUniqueId);
            if (studentOpt.isPresent()) {
                markStudentAttendance(studentOpt.get(), course, professorCourseOpt.get().getProfessor(), attendanceDate, true);
            }
        }

        // Mark attendance for absent students
        for (String studentUniqueId : absentStudentUniqueIds) {
            Optional<User> studentOpt = userRepository.findByUniqueId(studentUniqueId);
            if (studentOpt.isPresent()) {
                markStudentAttendance(studentOpt.get(), course, professorCourseOpt.get().getProfessor(), attendanceDate, false);
            }
        }
    }

    // Helper method to mark attendance for a student
    private void markStudentAttendance(User student, Course course, User professor, LocalDate attendanceDate, boolean status) {
        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setProfessor(professor);
        attendance.setAttendanceDate(attendanceDate);
        attendance.setStatus(status);
        attendanceRepository.save(attendance);
    }
}
