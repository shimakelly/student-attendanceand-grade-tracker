package auca.tracker.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import auca.tracker.domain.ClassGroup;

@Repository
public interface ClassGroupRepository extends JpaRepository<ClassGroup, UUID> {

    // D4: DERIVED findByGroupNumber
    Optional<ClassGroup> findByGroupNumber(int groupNumber);

    // C2: JPQL (NOT EXISTS subquery) - Free class groups sorted by groupNumber
    @Query("SELECT l FROM ClassGroup l WHERE NOT EXISTS (SELECT p FROM Teacher p WHERE p.classGroup = l) ORDER BY l.groupNumber ASC")
    List<ClassGroup> findFreeClassGroups();
}
