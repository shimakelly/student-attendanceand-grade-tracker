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
import auca.tracker.repository.ScoreEntryRepository;
import auca.tracker.repository.StudentRepository;
import auca.tracker.repository.TeacherRepository;

@Service
public class AssessmentService {
    @Autowired
    private AssessmentRepository assessmentRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    private ScoreEntryRepository scoreEntryRepo;

    public String saveAssessment(Assessment assessment) {
        if (assessment.getAssessmentDate() == null || assessment.getAssessmentDate().isBefore(LocalDate.now())) {
            return "Assessment date cannot be in the past";
        }
        if (assessment.getStudent() != null && assessment.getStudent().getId() != null) {
            studentRepo.findById(assessment.getStudent().getId()).ifPresent(assessment::setStudent);
        }
        if (assessment.getTeacher() != null && assessment.getTeacher().getId() != null) {
            teacherRepo.findById(assessment.getTeacher().getId()).ifPresent(assessment::setTeacher);
        }
        if (assessment.getTeacher() != null &&
            assessmentRepo.existsByTeacherAndAssessmentDateAndStatusNot(
                assessment.getTeacher(), assessment.getAssessmentDate(), AssessmentStatus.ABSENT)) {
            return "Teacher already has an assessment on that date";
        }
        assessment.setStatus(AssessmentStatus.PLANNED);
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
        existing.setTitle(updatedAssessment.getTitle());
        existing.setMaxScore(updatedAssessment.getMaxScore());
        if (updatedAssessment.getStudent() != null && updatedAssessment.getStudent().getId() != null) {
            studentRepo.findById(updatedAssessment.getStudent().getId()).ifPresent(existing::setStudent);
        }
        if (updatedAssessment.getTeacher() != null && updatedAssessment.getTeacher().getId() != null) {
            teacherRepo.findById(updatedAssessment.getTeacher().getId()).ifPresent(existing::setTeacher);
        }
        assessmentRepo.save(existing);
        return "Assessment updated successfully";
    }

    // A2 & E3: Delete and protect
    public String deleteAssessment(UUID id) {
        Optional<Assessment> optionalAssessment = assessmentRepo.findById(id);
        if (optionalAssessment.isEmpty()) {
            return "The assessment with that id does not exist";
        }
        Assessment existing = optionalAssessment.get();
        if (scoreEntryRepo.existsByAssessment(existing)) {
            return "That assessment has score entries, delete them first";
        }
        assessmentRepo.deleteById(id);
        return "Assessment deleted successfully";
    }

    public List<Assessment> getAssessmentsByStatus(AssessmentStatus status) {
        return assessmentRepo.findByStatusOrderByAssessmentDateAsc(status);
    }

    public List<Assessment> getAssessmentsBetween(LocalDate start, LocalDate end) {
        return assessmentRepo.findByAssessmentDateBetweenOrderByAssessmentDateAsc(start, end);
    }

    public String updateStatus(UUID id, AssessmentStatus newStatus) {
        Optional<Assessment> optionalAssessment = assessmentRepo.findById(id);
        if (optionalAssessment.isEmpty()) {
            return "The assessment with that id does not exist";
        }
        Assessment assessment = optionalAssessment.get();
        AssessmentStatus currentStatus = assessment.getStatus();

        boolean allowed = false;
        if (currentStatus == AssessmentStatus.PLANNED && (newStatus == AssessmentStatus.SAT || newStatus == AssessmentStatus.ABSENT)) {
            allowed = true;
        } else if (currentStatus == AssessmentStatus.SAT && (newStatus == AssessmentStatus.GRADED || newStatus == AssessmentStatus.ABSENT)) {
            allowed = true;
        }

        if (!allowed) {
            return "Cannot change status from " + currentStatus + " to " + newStatus;
        }

        assessment.setStatus(newStatus);
        assessmentRepo.save(assessment);
        return "Assessment status updated successfully";
    }
}
