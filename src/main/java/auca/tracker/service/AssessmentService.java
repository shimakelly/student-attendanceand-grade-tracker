package auca.tracker.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import auca.tracker.domain.Assessment;
import auca.tracker.domain.AssessmentStatus;
import auca.tracker.repository.AssessmentRepository;

@Service
public class AssessmentService {
    @Autowired
    private AssessmentRepository assessmentRepo;

    public String saveAssessment(Assessment assessment) {
        assessmentRepo.save(assessment);
        return "Assessment saved successfully";
    }

    public List<Assessment> getAllAssessments() {
        return assessmentRepo.findAll();
    }

    public Optional<Assessment> getAssessmentById(UUID id) {
        return assessmentRepo.findById(id);
    }

    public String updateAssessment(UUID id, Assessment updatedAssessment) {
        Optional<Assessment> optionalAssessment = assessmentRepo.findById(id);
        if (optionalAssessment.isEmpty()) {
            return "The assessment with that id does not exist";
        }
        Assessment existing = optionalAssessment.get();
        existing.setAssessmentDate(updatedAssessment.getAssessmentDate());
        existing.setStatus(updatedAssessment.getStatus());
        existing.setTitle(updatedAssessment.getTitle());
        existing.setMaxScore(updatedAssessment.getMaxScore());
        existing.setStudent(updatedAssessment.getStudent());
        existing.setTeacher(updatedAssessment.getTeacher());
        assessmentRepo.save(existing);
        return "Assessment updated successfully";
    }

    public String deleteAssessment(UUID id) {
        Optional<Assessment> optionalAssessment = assessmentRepo.findById(id);
        if (optionalAssessment.isEmpty()) {
            return "The assessment with that id does not exist";
        }
        assessmentRepo.deleteById(id);
        return "Assessment deleted successfully";
    }

    // B2: DERIVED (enum parameter, OrderBy) - Assessments by status
    public List<Assessment> getAssessmentsByStatus(AssessmentStatus status) {
        return assessmentRepo.findByStatusOrderByAssessmentDateAsc(status);
    }

    // B3: DERIVED (Between) - Assessments in a date range
    public List<Assessment> getAssessmentsBetween(LocalDate start, LocalDate end) {
        return assessmentRepo.findByAssessmentDateBetweenOrderByAssessmentDateAsc(start, end);
    }
}
