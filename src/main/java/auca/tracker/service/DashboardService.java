package auca.tracker.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.tracker.domain.AssessmentStatus;
import auca.tracker.repository.AssessmentRepository;
import auca.tracker.repository.ClassGroupRepository;
import auca.tracker.repository.ScoreEntryRepository;
import auca.tracker.repository.StudentRepository;
import auca.tracker.repository.TeacherRepository;

@Service
public class DashboardService {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    private ClassGroupRepository classGroupRepo;

    @Autowired
    private AssessmentRepository assessmentRepo;

    @Autowired
    private ScoreEntryRepository scoreEntryRepo;

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("students", studentRepo.count());
        summary.put("teachers", teacherRepo.count());
        summary.put("freeClassGroups", (long) classGroupRepo.findFreeClassGroups().size());
        summary.put("assessmentsToday", assessmentRepo.countByAssessmentDate(LocalDate.now()));
        summary.put("planned", assessmentRepo.countByStatus(AssessmentStatus.PLANNED));
        summary.put("scoreEntries", scoreEntryRepo.count());
        return summary;
    }
}
