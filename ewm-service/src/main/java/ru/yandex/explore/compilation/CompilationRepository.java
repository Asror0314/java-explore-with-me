package ru.yandex.explore.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query(value = "SELECT c.* from explore.compilation as c " +
            "WHERE c.pinned = COALESCE(?1, c.pinned) " +
            "LIMIT ?3 OFFSET ?2", nativeQuery = true)
    List<Compilation> getCompilations(Boolean pinned, int from, int size);
}
