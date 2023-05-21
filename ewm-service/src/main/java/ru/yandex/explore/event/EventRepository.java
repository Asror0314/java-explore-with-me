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
            "WHERE e.initiator_id in :users " +
            "AND e.state in :states " +
            "AND e.category_id in :categories " +
            "AND e.event_date BETWEEN :rangeStart AND :rangeEnd " +
            "LIMIT :size OFFSET :from", nativeQuery = true)
    List<Event> findAllAdmin(
            @Param(value = "users") Set<Long> users,
            @Param(value = "states") Set<String> states,
            @Param(value = "categories") Set<Long> categories,
            @Param(value = "rangeStart") LocalDateTime rangeStart,
            @Param(value = "rangeEnd") LocalDateTime rangeEnd,
            @Param(value = "from") int from,
            @Param(value = "size") int size
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Event as e SET " +
            "e.annotation=COALESCE(:annotation, e.annotation), " +
            "e.category=COALESCE(:category, e.category), " +
            "e.createdOn=COALESCE(:createdOn, e.createdOn), " +
            "e.description=COALESCE(:description, e.description), " +
            "e.eventDate=COALESCE(:eventDate, e.eventDate), " +
            "e.location=COALESCE(:location, e.location), " +
            "e.paid=COALESCE(:paid, e.paid), " +
            "e.participantLimit=COALESCE(:participantLimit, e.participantLimit), " +
            "e.requestModeration=COALESCE(:requestModeration, e.requestModeration), " +
            "e.state=:state, " +
            "e.title=COALESCE(:title, e.title) " +
            "WHERE e.id =:id")
    void updateEventUser(
            @Param(value = "id") Long id,
            @Param(value = "annotation") String annotation,
            @Param(value = "category") Category category,
            @Param(value = "createdOn") LocalDateTime createdOn,
            @Param(value = "description") String description,
            @Param(value = "eventDate") LocalDateTime eventDate,
            @Param(value = "location") Location location,
            @Param(value = "paid") Boolean paid,
            @Param(value = "participantLimit") Integer participantLimit,
            @Param(value = "requestModeration") Boolean requestModeration,
            @Param(value = "state") EventState state,
            @Param(value = "title") String title
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Event as e SET " +
            "e.annotation=COALESCE(:annotation, e.annotation), " +
            "e.category=COALESCE(:category, e.category), " +
            "e.createdOn=COALESCE(:createdOn, e.createdOn), " +
            "e.description=COALESCE(:description, e.description), " +
            "e.eventDate=COALESCE(:eventDate, e.eventDate), " +
            "e.location=COALESCE(:location, e.location), " +
            "e.paid=COALESCE(:paid, e.paid), " +
            "e.participantLimit=COALESCE(:participantLimit, e.participantLimit), " +
            "e.requestModeration=COALESCE(:requestModeration, e.requestModeration), " +
            "e.publishedOn=:date, " +
            "e.state=:state, " +
            "e.title=COALESCE(:title, e.title) " +
            "WHERE e.id =:id")
    void updateEventAdmin(
            @Param(value = "id") Long id,
            @Param(value = "annotation") String annotation,
            @Param(value = "category") Category category,
            @Param(value = "createdOn") LocalDateTime createdOn,
            @Param(value = "description") String description,
            @Param(value = "eventDate") LocalDateTime eventDate,
            @Param(value = "location") Location location,
            @Param(value = "paid") Boolean paid,
            @Param(value = "participantLimit") Integer participantLimit,
            @Param(value = "requestModeration") Boolean requestModeration,
            @Param(value = "date") LocalDateTime publishedOn,
            @Param(value = "state") EventState state,
            @Param(value = "title") String title
    );

    @Query(value = "SELECT e.* FROM explore.event AS e " +
            "WHERE (e.annotation ilike COALESCE(:text, e.annotation) " +
            "OR e.description ilike COALESCE(:text, e.description)) " +
            "AND e.category_id in :categories " +
            "AND e.paid = COALESCE(:paid, e.paid) " +
            "AND e.state = 'PUBLISHED' " +
            "AND e.event_date BETWEEN :rangeStart AND :rangeEnd " +
            "ORDER BY e.event_date ASC " +
            "LIMIT :size OFFSET :from", nativeQuery = true)
    List<Event> findAllPublishWithSortEventDate(
            @Param(value = "text") String text,
            @Param(value = "categories") Set<Long> categories,
            @Param(value = "paid") Boolean paid,
            @Param(value = "rangeStart") LocalDateTime rangeStart,
            @Param(value = "rangeEnd") LocalDateTime rangeEnd,
            @Param(value = "from") int from,
            @Param(value = "size") int size
    );
}
