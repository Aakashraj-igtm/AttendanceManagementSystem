package com.attendance.management.repo;



import com.attendance.management.entity.ProfessorCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfessorCourseRepository extends JpaRepository<ProfessorCourse, Long> {

    @Query("SELECT pc FROM ProfessorCourse pc " +
            "JOIN pc.professor p " +
            "JOIN pc.course c " +
            "WHERE p.uniqueId = :professorUniqueId AND c.courseId = :courseId")
    Optional<ProfessorCourse> findByProfessorAndCourse(@Param("professorUniqueId") String professorUniqueId,
                                                       @Param("courseId") Long courseId);
    boolean existsByProfessor_UserIdAndCourse_CourseId(Long professorId, Long courseId);
}
