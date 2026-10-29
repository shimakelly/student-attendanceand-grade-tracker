package auca.tracker.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import auca.tracker.domain.Student;
import auca.tracker.repository.StudentRepository;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepo;

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
}
