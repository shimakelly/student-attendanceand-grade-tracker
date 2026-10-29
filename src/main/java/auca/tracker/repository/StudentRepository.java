package auca.tracker.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import auca.tracker.domain.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    // B1: DERIVED (IgnoreCase, OrderBy) - Find students by lastName
    List<Student> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    Optional<Student> findByStudentNumber(String studentNumber);
}
