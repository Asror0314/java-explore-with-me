package ru.yandex.explore.request;

import ru.yandex.explore.request.dto.EventRequestStatusUpdateResultDto;
import ru.yandex.explore.request.dto.RequestDto;
import ru.yandex.explore.request.dto.EventRequestStatusUpdateRequest;

import java.util.List;

public interface RequestService {
    RequestDto addNewRequest(Long requesterId, Long eventId);

    RequestDto cancelRequestRequester(Long requesterId, Long requestId);

    List<RequestDto> getRequestRequester(Long requesterId);

    List<RequestDto> getRequestInitiator(Long initiatorId, Long eventId);

    EventRequestStatusUpdateResultDto updateRequestStatusInitiator(
            Long initiatorId,
            Long eventId,
            EventRequestStatusUpdateRequest requestInitiatorDto);
}
