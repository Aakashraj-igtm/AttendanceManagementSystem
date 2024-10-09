package com.attendance.management.service;


import com.attendance.management.dto.AttendancePercentageResponseDto;
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

    // Method to update attendance based on date, list of present students, courseNo, professorUniqueId
    public void updateAttendance(String professorUniqueId, String courseNo, LocalDate attendanceDate, List<String> presentStudentUniqueIds) {
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

        User professor = professorCourseOpt.get().getProfessor();

        // Process each student in the list
        for (String studentUniqueId : presentStudentUniqueIds) {
            Optional<User> studentOpt = userRepository.findByUniqueId(studentUniqueId);
            if (studentOpt.isPresent()) {
                User student = studentOpt.get();

                // Find the existing attendance record for the student, course, professor, and date
                Optional<Attendance> existingAttendanceOpt = attendanceRepository.findByStudentAndCourseAndProfessorAndAttendanceDate(student, course, professor, attendanceDate);

                if (existingAttendanceOpt.isPresent()) {
                    // Update existing record
                    Attendance existingAttendance = existingAttendanceOpt.get();
                    existingAttendance.setStatus(true);  // Mark as present
                    attendanceRepository.save(existingAttendance);
                } else {
                    // Create new attendance record if it doesn't exist
                    markStudentAttendance(student,course,professor,attendanceDate,true);
                }
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

    // Calculate student's attendance percentage for a course
    public AttendancePercentageResponseDto calculateAttendance(String studentUniqueId, String courseNo) {
        AttendancePercentageResponseDto response = new AttendancePercentageResponseDto();

        // Find the course by courseNo
        Optional<Course> courseOpt = courseRepository.findByCourseNo(courseNo);
        if (courseOpt.isEmpty()) {
            throw new RuntimeException("Course not found with courseNo: " + courseNo);
        }

        Course course = courseOpt.get();

        // Find the student by studentUniqueId
        Optional<User> studentOpt = userRepository.findByUniqueId(studentUniqueId);
        if (studentOpt.isEmpty() || !"Student".equals(studentOpt.get().getRole().getName())) {
            throw new RuntimeException("Student not found with unique ID: " + studentUniqueId);
        }

        User student = studentOpt.get();

        // Find the professor-course relationship to get the minimum attendance percentage
        Optional<ProfessorCourse> professorCourseOpt = professorCourseRepository.findByCourse_CourseId(course.getCourseId());
        if (professorCourseOpt.isEmpty()) {
            throw new RuntimeException("Professor is not assigned to this course.");
        }

        ProfessorCourse professorCourse = professorCourseOpt.get();
        double minAttendancePercentage = professorCourse.getMinAttendancePercentage();
        response.setMinAttendancePercentage(minAttendancePercentage);  // Set minAttendancePercentage in the response

        // Fetch student's enrollment date (we assume this date is when the student was first enrolled in the course)
        LocalDate enrollmentDate = student.getCreatedAt().toLocalDate();  // Assuming the createdAt is when the student enrolled

        // Total number of classes recorded for this course since the student's enrollment
        long totalClasses = attendanceRepository.countTotalRecordedClasses(course, enrollmentDate);

        // Total number of classes attended by the student
        long classesAttended = attendanceRepository.countClassesAttendedByStudent(course, student, enrollmentDate);

        // If there are no recorded classes, return a response indicating no attendance data
        if (totalClasses == 0) {
            response.setAttendancePercentage(0.0);
            response.setMessage("No attendance records available for this course.");
            return response;
        }

        // Calculate attendance percentage
        double attendancePercentage = (double) classesAttended / totalClasses * 100;
        response.setAttendancePercentage(attendancePercentage);

        // Compare with minimum attendance percentage and set message
        if (attendancePercentage >= minAttendancePercentage) {
            response.setMessage("Attendance percentage (" + attendancePercentage + "%) meets the required minimum of " + minAttendancePercentage + "%.");
        } else {
            response.setMessage("Attendance percentage (" + attendancePercentage + "%) is below the required minimum of " + minAttendancePercentage + "%.");
        }

        return response;
    }
}
