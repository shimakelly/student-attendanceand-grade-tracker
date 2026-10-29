package auca.tracker.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import auca.tracker.domain.Assessment;
import auca.tracker.domain.AssessmentStatus;
import auca.tracker.service.AssessmentService;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {
    @Autowired
    private AssessmentService assessmentService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveAssessment(@RequestBody Assessment assessment) {
        String message = assessmentService.saveAssessment(assessment);
        if (message.contains("past")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        if (message.contains("already has")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAssessments() {
        List<Assessment> assessments = assessmentService.getAllAssessments();
        return new ResponseEntity<>(assessments, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAssessmentById(@PathVariable UUID id) {
        Optional<Assessment> assessment = assessmentService.getAssessmentById(id);
        if (assessment.isEmpty()) {
            return new ResponseEntity<>("The assessment with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(assessment.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAssessment(@PathVariable UUID id, @RequestBody Assessment assessment) {
        String message = assessmentService.updateAssessment(id, assessment);
        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAssessment(@PathVariable UUID id) {
        String message = assessmentService.deleteAssessment(id);
        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if (message.contains("delete them first")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping(value = "/by-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAssessmentsByStatus(@RequestParam AssessmentStatus status) {
        List<Assessment> assessments = assessmentService.getAssessmentsByStatus(status);
        return new ResponseEntity<>(assessments, HttpStatus.OK);
    }

    @GetMapping(value = "/between", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAssessmentsBetween(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        List<Assessment> assessments = assessmentService.getAssessmentsBetween(startDate, endDate);
        return new ResponseEntity<>(assessments, HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateStatus(@PathVariable UUID id, @RequestParam AssessmentStatus status) {
        String message = assessmentService.updateStatus(id, status);
        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if (message.contains("Cannot change status")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // F1: Assessments per status
    @GetMapping(value = "/stats/by-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStatsByStatus() {
        List<Object[]> stats = assessmentService.getStatsByStatus();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    // F2: Teacher is absent
    @PatchMapping(value = "/cancel-day", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cancelDay(@RequestParam UUID teacherId, @RequestParam String date) {
        LocalDate cancelDate = LocalDate.parse(date);
        String message = assessmentService.cancelDayForTeacher(teacherId, cancelDate);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
