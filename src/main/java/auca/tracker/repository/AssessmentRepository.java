package auca.tracker.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import auca.tracker.domain.Assessment;
import auca.tracker.domain.AssessmentStatus;
import auca.tracker.domain.Student;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {
    List<Assessment> findByStatusOrderByAssessmentDateAsc(AssessmentStatus status);
    List<Assessment> findByAssessmentDateBetweenOrderByAssessmentDateAsc(LocalDate start, LocalDate end);

    // C4: JPQL (DISTINCT, path navigation)
    @Query("SELECT DISTINCT a.student FROM Assessment a WHERE a.teacher.id = :teacherId")
    List<Student> findStudentsByTeacherId(@Param("teacherId") UUID teacherId);
}
