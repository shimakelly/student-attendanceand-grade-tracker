package auca.tracker.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import auca.tracker.domain.Assessment;
import auca.tracker.domain.AssessmentStatus;
import auca.tracker.domain.Student;
import auca.tracker.domain.Teacher;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {

    // B2: DERIVED (enum parameter, OrderBy)
    List<Assessment> findByStatusOrderByAssessmentDateAsc(AssessmentStatus status);

    // B3: DERIVED (Between)
    List<Assessment> findByAssessmentDateBetweenOrderByAssessmentDateAsc(LocalDate start, LocalDate end);

    // C4: JPQL (DISTINCT, path navigation)
    @Query("SELECT DISTINCT a.student FROM Assessment a WHERE a.teacher.id = :teacherId")
    List<Student> findStudentsByTeacherId(@Param("teacherId") UUID teacherId);

    // D1: DERIVED (existsBy, And, Not)
    boolean existsByTeacherAndAssessmentDateAndStatusNot(Teacher teacher, LocalDate assessmentDate, AssessmentStatus status);

    // F1: JPQL (GROUP BY, COUNT)
    @Query("SELECT a.status, COUNT(a) FROM Assessment a GROUP BY a.status")
    List<Object[]> countAssessmentsPerStatus();

    // F2: JPQL (UPDATE, @Modifying, @Transactional)
    @Modifying
    @Query("UPDATE Assessment a SET a.status = :absentStatus WHERE a.teacher.id = :teacherId AND a.assessmentDate = :date AND a.status != :gradedStatus")
    int cancelDayForTeacher(
        @Param("teacherId") UUID teacherId,
        @Param("date") LocalDate date,
        @Param("absentStatus") AssessmentStatus absentStatus,
        @Param("gradedStatus") AssessmentStatus gradedStatus
    );

    // Bonus 1: DERIVED countBy
    long countByAssessmentDate(LocalDate assessmentDate);
    long countByStatus(AssessmentStatus status);
}
