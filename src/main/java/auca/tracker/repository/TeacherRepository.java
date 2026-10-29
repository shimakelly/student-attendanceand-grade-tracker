package auca.tracker.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import auca.tracker.domain.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, UUID> {
}
