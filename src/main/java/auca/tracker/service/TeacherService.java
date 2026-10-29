package auca.tracker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.tracker.domain.ClassGroup;
import auca.tracker.domain.Course;
import auca.tracker.domain.Teacher;
import auca.tracker.repository.ClassGroupRepository;
import auca.tracker.repository.CourseRepository;
import auca.tracker.repository.TeacherRepository;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    private ClassGroupRepository classGroupRepo;

    @Autowired
    private CourseRepository courseRepo;

    public String saveTeacher(Teacher teacher) {
        if (teacher.getClassGroup() != null && teacher.getClassGroup().getId() != null) {
            Optional<ClassGroup> cg = classGroupRepo.findById(teacher.getClassGroup().getId());
            cg.ifPresent(teacher::setClassGroup);
        }
        if (teacher.getCourses() != null && !teacher.getCourses().isEmpty()) {
            List<Course> attachedCourses = new ArrayList<>();
            for (Course c : teacher.getCourses()) {
                if (c.getId() != null) {
                    courseRepo.findById(c.getId()).ifPresent(attachedCourses::add);
                }
            }
            teacher.setCourses(attachedCourses);
        }
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
        if (updatedTeacher.getClassGroup() != null && updatedTeacher.getClassGroup().getId() != null) {
            classGroupRepo.findById(updatedTeacher.getClassGroup().getId()).ifPresent(existing::setClassGroup);
        }
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

    // D3: Add one course to a teacher
    public String addCourseToTeacher(UUID teacherId, UUID courseId) {
        Optional<Teacher> optionalTeacher = teacherRepo.findById(teacherId);
        if (optionalTeacher.isEmpty()) {
            return "The teacher with that id does not exist";
        }
        Optional<Course> optionalCourse = courseRepo.findById(courseId);
        if (optionalCourse.isEmpty()) {
            return "That course does not exist";
        }

        Teacher teacher = optionalTeacher.get();
        Course course = optionalCourse.get();

        for (Course existingCourse : teacher.getCourses()) {
            if (existingCourse.getId().equals(course.getId())) {
                return "The teacher already has that course";
            }
        }

        teacher.getCourses().add(course);
        teacherRepo.save(teacher);
        return "Course added to the teacher";
    }

    // D4: Move a teacher to another class group
    public String changeClassGroup(UUID teacherId, int groupNumber) {
        Optional<Teacher> optionalTeacher = teacherRepo.findById(teacherId);
        if (optionalTeacher.isEmpty()) {
            return "The teacher with that id does not exist";
        }
        Optional<ClassGroup> optionalClassGroup = classGroupRepo.findByGroupNumber(groupNumber);
        if (optionalClassGroup.isEmpty()) {
            return "The class group with that groupNumber does not exist";
        }

        ClassGroup targetGroup = optionalClassGroup.get();
        if (teacherRepo.existsByClassGroup(targetGroup)) {
            return "That class group is taken by another teacher";
        }

        Teacher teacher = optionalTeacher.get();
        teacher.setClassGroup(targetGroup);
        teacherRepo.save(teacher);
        return "Class group changed successfully";
    }
}
