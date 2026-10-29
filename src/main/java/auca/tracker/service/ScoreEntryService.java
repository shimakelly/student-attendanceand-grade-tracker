package auca.tracker.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.tracker.domain.Assessment;
import auca.tracker.domain.AssessmentStatus;
import auca.tracker.domain.ScoreEntry;
import auca.tracker.repository.AssessmentRepository;
import auca.tracker.repository.ScoreEntryRepository;

@Service
public class ScoreEntryService {

    @Autowired
    private ScoreEntryRepository scoreEntryRepo;

    @Autowired
    private AssessmentRepository assessmentRepo;

    // E1: Write a score entry
    public String saveScoreEntry(ScoreEntry scoreEntry, UUID assessmentId) {
        Optional<Assessment> optionalAssessment = assessmentRepo.findById(assessmentId);
        if (optionalAssessment.isEmpty()) {
            return "The assessment with that id does not exist";
        }

        Assessment assessment = optionalAssessment.get();
        if (assessment.getStatus() != AssessmentStatus.GRADED) {
            return "Score entries are only for GRADED assessments";
        }

        scoreEntry.setEnteredDate(LocalDate.now());
        scoreEntry.setAssessment(assessment);
        scoreEntryRepo.save(scoreEntry);
        return "Score entry saved successfully";
    }

    // E2: Score entry history of a student
    public List<ScoreEntry> getScoreEntriesByStudentNumber(String studentNumber) {
        return scoreEntryRepo.findByStudentNumber(studentNumber);
    }

    // E3: Delete score entry
    public String deleteScoreEntry(UUID id) {
        Optional<ScoreEntry> optionalScoreEntry = scoreEntryRepo.findById(id);
        if (optionalScoreEntry.isEmpty()) {
            return "The score entry with that id does not exist";
        }
        scoreEntryRepo.deleteById(id);
        return "Score entry deleted successfully";
    }

    public List<ScoreEntry> getAllScoreEntries() {
        return scoreEntryRepo.findAll();
    }

    public Optional<ScoreEntry> getScoreEntryById(UUID id) {
        return scoreEntryRepo.findById(id);
    }
}
