package ru.yandex.explore.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
}
