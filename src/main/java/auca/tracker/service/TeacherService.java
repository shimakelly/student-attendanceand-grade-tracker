package auca.tracker.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import auca.tracker.domain.Teacher;
import auca.tracker.repository.TeacherRepository;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepo;

    public String saveTeacher(Teacher teacher) {
        teacherRepo.save(teacher);
        return "Teacher saved successfully";
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepo.findAll();
    }

    public Optional<Teacher> getTeacherById(UUID id) {
        return teacherRepo.findById(id);
    }

    public String updateTeacher(UUID id, Teacher updatedTeacher) {
        Optional<Teacher> optionalTeacher = teacherRepo.findById(id);
        if (optionalTeacher.isEmpty()) {
            return "The teacher with that id does not exist";
        }
        Teacher existing = optionalTeacher.get();
        existing.setFirstName(updatedTeacher.getFirstName());
        existing.setLastName(updatedTeacher.getLastName());
        existing.setEmail(updatedTeacher.getEmail());
        teacherRepo.save(existing);
        return "Teacher updated successfully";
    }

    public String deleteTeacher(UUID id) {
        Optional<Teacher> optionalTeacher = teacherRepo.findById(id);
        if (optionalTeacher.isEmpty()) {
            return "The teacher with that id does not exist";
        }
        teacherRepo.deleteById(id);
        return "Teacher deleted successfully";
    }

    // C1: JPQL (JOIN on collection, LOWER) - Teachers by course name
    public List<Teacher> getTeachersByCourse(String courseName) {
        return teacherRepo.findByCourseName(courseName);
    }
}
