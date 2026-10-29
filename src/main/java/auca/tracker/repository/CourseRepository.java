package auca.tracker.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import auca.tracker.domain.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    Optional<Course> findByName(String name);

    // C3: JPQL (IS EMPTY) - Courses nobody holds
    @Query("SELECT c FROM Course c WHERE c.teachers IS EMPTY")
    List<Course> findUnusedCourses();
}
