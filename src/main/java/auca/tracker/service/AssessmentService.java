package auca.tracker.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // A2 & D1: Safe saving
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

        // A new assessment always starts as PLANNED, whatever status the JSON body contains
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

    // A2 & D2: PUT update must no longer change the status
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
        // Status is not changed here; only changed via D2 status flow endpoint
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

    // B2: DERIVED (enum parameter, OrderBy) - Assessments by status
    public List<Assessment> getAssessmentsByStatus(AssessmentStatus status) {
        return assessmentRepo.findByStatusOrderByAssessmentDateAsc(status);
    }

    // B3: DERIVED (Between) - Assessments in a date range
    public List<Assessment> getAssessmentsBetween(LocalDate start, LocalDate end) {
        return assessmentRepo.findByAssessmentDateBetweenOrderByAssessmentDateAsc(start, end);
    }

    // D2: Status flow
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

    // F1: JPQL (GROUP BY, COUNT) - Assessments per status
    public List<Object[]> getStatsByStatus() {
        return assessmentRepo.countAssessmentsPerStatus();
    }

    // F2: JPQL (UPDATE, @Modifying, @Transactional) - Teacher is absent
    @Transactional
    public String cancelDayForTeacher(UUID teacherId, LocalDate date) {
        int updatedCount = assessmentRepo.cancelDayForTeacher(
            teacherId, date, AssessmentStatus.ABSENT, AssessmentStatus.GRADED
        );
        return updatedCount + " assessments cancelled";
    }

    // Bonus 2: Assessments page by page
    public Page<Assessment> getAssessmentsPage(Pageable pageable) {
        return assessmentRepo.findAll(pageable);
    }
}
