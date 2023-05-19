package ru.yandex.explore.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.explore.category.Category;
import ru.yandex.explore.event.dto.EventState;
import ru.yandex.explore.location.Location;

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
    List<Event> findAllAdmin(
            Set<Integer> users,
            Set<String> states,
            Set<Integer> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Event as e SET " +
            "e.annotation=:annotation, " +
            "e.category=:category, " +
            "e.description=:description, " +
            "e.eventDate=:eventDate, " +
            "e.location=:location, " +
            "e.paid=:paid, " +
            "e.participantLimit=:participantLimit, " +
            "e.requestModeration=:requestModeration, " +
            "e.state=:state, " +
            "e.title=:title " +
            "WHERE e.id =:id")
    void updateEvent(
            @Param(value = "id") Long id,
            @Param(value = "annotation") String annotation,
            @Param(value = "category") Category category,
            @Param(value = "description") String description,
            @Param(value = "eventDate") LocalDateTime eventDate,
            @Param(value = "location") Location location,
            @Param(value = "paid") boolean paid,
            @Param(value = "participantLimit") int participantLimit,
            @Param(value = "requestModeration") boolean requestModeration,
            @Param(value = "state") EventState state,
            @Param(value = "title") String title
    );
}
