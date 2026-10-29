package auca.tracker.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import auca.tracker.domain.Assessment;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {
}
