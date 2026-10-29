package auca.tracker.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import auca.tracker.domain.Assessment;
import auca.tracker.domain.ScoreEntry;

@Repository
public interface ScoreEntryRepository extends JpaRepository<ScoreEntry, UUID> {

    // E2: JPQL (path navigation, ORDER BY) - Score entry history of a student
    @Query("SELECT s FROM ScoreEntry s WHERE s.assessment.student.studentNumber = :studentNumber ORDER BY s.enteredDate DESC")
    List<ScoreEntry> findByStudentNumber(@Param("studentNumber") String studentNumber);

    // E3: DERIVED existsByAssessment
    boolean existsByAssessment(Assessment assessment);
}
