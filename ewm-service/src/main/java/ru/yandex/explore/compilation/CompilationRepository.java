package ru.yandex.explore.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.explore.event.Event;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query(value = "SELECT c.* from explore.compilation as c " +
            "WHERE c.pinned = COALESCE(?1, c.pinned) " +
            "LIMIT ?3 OFFSET ?2", nativeQuery = true)
    List<Compilation> getCompilations(Boolean pinned, int from, int size);
}
