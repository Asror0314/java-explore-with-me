package ru.yandex.explore.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "SELECT e.* from explore.event AS e " +
            "WHERE e.initiator_id = ?1 " +
            "LIMIT ?3 OFFSET ?2", nativeQuery = true)
    List<Event> findAllByInitiator(Long initiatorId, int from, int size);

    @Query(value = "SELECT e.* FROM explore.event AS e " +
            "WHERE e.initiator_id IN ?1 " +
            "AND e.state IN ?2 " +
            "AND e.category_id IN ?3 " +
            "AND e.event_date BETWEEN ?4 AND ?5 " +
            "LIMIT ?7 OFFSET ?6", nativeQuery = true)
    List<Event> findAllAdmin(Set<Integer> users, Set<String> states, Set<Integer> categories,
                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);
}
