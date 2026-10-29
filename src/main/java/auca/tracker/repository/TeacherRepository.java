package auca.tracker.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import auca.tracker.domain.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, UUID> {
    // C1: JPQL (JOIN on collection, LOWER) - Teachers by course name
    @Query("SELECT t FROM Teacher t JOIN t.courses c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Teacher> findByCourseName(@Param("name") String name);
}
