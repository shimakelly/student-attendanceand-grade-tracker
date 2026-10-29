package auca.tracker.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.tracker.domain.Student;
import auca.tracker.repository.AssessmentRepository;
import auca.tracker.repository.StudentRepository;
import auca.tracker.repository.TeacherRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    private AssessmentRepository assessmentRepo;

    public String saveStudent(Student student) {
        studentRepo.save(student);
        return "Student saved successfully";
    }

    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public Optional<Student> getStudentById(UUID id) {
        return studentRepo.findById(id);
    }

    public String updateStudent(UUID id, Student updatedStudent) {
        Optional<Student> optionalStudent = studentRepo.findById(id);
        if (optionalStudent.isEmpty()) {
            return "The student with that id does not exist";
        }
        Student existing = optionalStudent.get();
        existing.setFirstName(updatedStudent.getFirstName());
        existing.setLastName(updatedStudent.getLastName());
        existing.setStudentNumber(updatedStudent.getStudentNumber());
        existing.setEmail(updatedStudent.getEmail());
        studentRepo.save(existing);
        return "Student updated successfully";
    }

    public String deleteStudent(UUID id) {
        Optional<Student> optionalStudent = studentRepo.findById(id);
        if (optionalStudent.isEmpty()) {
            return "The student with that id does not exist";
        }
        studentRepo.deleteById(id);
        return "Student deleted successfully";
    }

    // B1: DERIVED (IgnoreCase, OrderBy) - Find students by lastName
    public List<Student> getStudentsByLastName(String lastName) {
        return studentRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    // C4: JPQL (DISTINCT, path navigation) - Students of a teacher
    public Optional<List<Student>> getStudentsOfTeacher(UUID teacherId) {
        if (!teacherRepo.existsById(teacherId)) {
            return Optional.empty();
        }
        List<Student> students = assessmentRepo.findStudentsByTeacherId(teacherId);
        return Optional.of(students);
    }
}
