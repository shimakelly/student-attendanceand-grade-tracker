package auca.tracker.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import auca.tracker.domain.Teacher;
import auca.tracker.service.TeacherService;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveTeacher(@RequestBody Teacher teacher) {
        String message = teacherService.saveTeacher(teacher);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTeacherById(@PathVariable UUID id) {
        Optional<Teacher> teacher = teacherService.getTeacherById(id);
        if (teacher.isEmpty()) {
            return new ResponseEntity<>("The teacher with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(teacher.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTeacher(@PathVariable UUID id, @RequestBody Teacher teacher) {
        String message = teacherService.updateTeacher(id, teacher);
        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteTeacher(@PathVariable UUID id) {
        String message = teacherService.deleteTeacher(id);
        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // C1: Teachers by course
    @GetMapping(value = "/by-course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTeachersByCourse(@RequestParam String name) {
        List<Teacher> teachers = teacherService.getTeachersByCourse(name);
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }

    // D3: Add one course to a teacher
    @PutMapping(value = "/{teacherId}/courses/{courseId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCourseToTeacher(@PathVariable UUID teacherId, @PathVariable UUID courseId) {
        String message = teacherService.addCourseToTeacher(teacherId, courseId);
        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if (message.contains("already has")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // D4: Move a teacher to another class group
    @PatchMapping(value = "/{teacherId}/class-group", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeClassGroup(@PathVariable UUID teacherId, @RequestParam int groupNumber) {
        String message = teacherService.changeClassGroup(teacherId, groupNumber);
        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if (message.contains("taken by another teacher")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
