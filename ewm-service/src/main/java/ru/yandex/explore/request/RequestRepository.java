package ru.yandex.explore.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.explore.event.Event;
import ru.yandex.explore.user.User;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT count(r.id) FROM Request as r " +
            "WHERE r.requester = ?1 AND r.event = ?2")
    int findAllByRequesterAndEvent(User requester, Event event);

    List<Request> findAllByRequester(User requester);

    @Query("SELECT r FROM Request AS r " +
            "WHERE r.event.initiator=:initiator " +
            "AND r.event.id=:eventId ")
    List<Request> findAllByInitiator(
            @Param(value = "initiator") User initiator,
            @Param(value = "eventId") Long eventId);

    @Query("SELECT r FROM Request As r " +
            "where r.status = 'PENDING'")
    List<Request> findAllPendingStatus();
}
