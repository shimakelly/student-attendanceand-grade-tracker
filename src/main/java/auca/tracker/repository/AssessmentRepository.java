package auca.tracker.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import auca.tracker.domain.Assessment;
import auca.tracker.domain.AssessmentStatus;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {
    // B2: DERIVED (enum parameter, OrderBy)
    List<Assessment> findByStatusOrderByAssessmentDateAsc(AssessmentStatus status);

    // B3: DERIVED (Between)
    List<Assessment> findByAssessmentDateBetweenOrderByAssessmentDateAsc(LocalDate start, LocalDate end);
}
