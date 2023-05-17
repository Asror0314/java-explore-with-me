package ru.yandex.explore.request;

import ru.yandex.explore.request.dto.ParticipationRequestDto;

public interface ParticipationRequestService {
    ParticipationRequestDto addNewRequest(Long requesterId, Long eventId);
}
