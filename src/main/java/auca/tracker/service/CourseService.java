package auca.tracker.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import auca.tracker.domain.Course;
import auca.tracker.repository.CourseRepository;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepo;

    public String saveCourse(Course course) {
        courseRepo.save(course);
        return "Course saved successfully";
    }

    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }

    public Optional<Course> getCourseById(UUID id) {
        return courseRepo.findById(id);
    }

    public String updateCourse(UUID id, Course updatedCourse) {
        Optional<Course> optionalCourse = courseRepo.findById(id);
        if (optionalCourse.isEmpty()) {
            return "The course with that id does not exist";
        }
        Course existingCourse = optionalCourse.get();
        existingCourse.setName(updatedCourse.getName());
        courseRepo.save(existingCourse);
        return "Course updated successfully";
    }

    public String deleteCourse(UUID id) {
        Optional<Course> optionalCourse = courseRepo.findById(id);
        if (optionalCourse.isEmpty()) {
            return "The course with that id does not exist";
        }
        courseRepo.deleteById(id);
        return "Course deleted successfully";
    }
}
