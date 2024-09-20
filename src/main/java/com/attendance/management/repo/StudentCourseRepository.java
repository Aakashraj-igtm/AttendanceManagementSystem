package com.attendance.management.repo;

import com.attendance.management.dto.StudentResponseDto;
import com.attendance.management.entity.StudentCourse;
import com.attendance.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {

    boolean existsByStudent_UserIdAndCourse_CourseId(Long studentId, Long courseId);

    @Query("SELECT sc.student FROM StudentCourse sc " +
            "JOIN ProfessorCourse pc ON sc.course.courseId = pc.course.courseId " +
            "WHERE pc.professor.userId = :professorId AND pc.course.courseId = :courseId")
    List<User> findEnrolledStudentsByProfessorAndCourse(@Param("professorId") Long professorId, @Param("courseId") Long courseId);

    @Query("SELECT new com.attendance.management.dto.StudentResponseDto(sc.student.firstName, sc.student.lastName, sc.student.uniqueId, sc.student.email) " +
            "FROM StudentCourse sc " +
            "JOIN ProfessorCourse pc ON sc.course.courseId = pc.course.courseId " +
            "JOIN Course c ON sc.course.courseId = c.courseId " +
            "JOIN User p ON pc.professor.userId = p.userId " +
            "WHERE c.courseNo = :courseNo AND p.uniqueId = :professorUniqueId")
    List<StudentResponseDto> findStudentsByCourseNoAndProfessorUniqueId(
            @Param("courseNo") String courseNo,
            @Param("professorUniqueId") String professorUniqueId);
}
