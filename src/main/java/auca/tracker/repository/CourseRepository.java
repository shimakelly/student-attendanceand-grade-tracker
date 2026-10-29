package auca.tracker.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import auca.tracker.domain.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    Optional<Course> findByName(String name);
}
