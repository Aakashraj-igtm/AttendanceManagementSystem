package com.attendance.management.repo;

import com.attendance.management.entity.Attendance;
import com.attendance.management.entity.Course;
import com.attendance.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByStudentAndCourseAndProfessorAndAttendanceDate(User student, Course course, User professor, LocalDate attendanceDate);

    // Count total classes recorded for the course (only where attendance entries exist)
    @Query("SELECT COUNT(DISTINCT a.attendanceDate) FROM Attendance a WHERE a.course = :course AND a.attendanceDate >= :enrollmentDate")
    long countTotalRecordedClasses(@Param("course") Course course, @Param("enrollmentDate") LocalDate enrollmentDate);

    // Count classes the student attended (only for dates where attendance records exist)
    @Query("SELECT COUNT(DISTINCT a.attendanceDate) FROM Attendance a WHERE a.course = :course AND a.student = :student AND a.status = true AND a.attendanceDate >= :enrollmentDate")
    long countClassesAttendedByStudent(@Param("course") Course course, @Param("student") User student, @Param("enrollmentDate") LocalDate enrollmentDate);
}
