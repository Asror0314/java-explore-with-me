package ru.yandex.explore.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.explore.request.dto.EventRequestStatusUpdateResultDto;
import ru.yandex.explore.request.dto.RequestDto;
import ru.yandex.explore.request.dto.EventRequestStatusUpdateRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class RequestController {
    private final RequestService service;

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto addNewRequest(
            @PathVariable(name = "userId") Long requesterId,
            @RequestParam(name = "eventId") Long eventId
    ) {
        log.info("Creating new participation request with userId={}, eventId={}", requesterId, eventId);
        return service.addNewRequest(requesterId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public RequestDto cancelRequestRequester(
            @PathVariable(name = "userId") Long requesterId,
            @PathVariable(name = "requestId") Long requestId) {
        log.info("Canceled participation request with requesterId={}, requestId={}", requesterId, requestId);
        return service.cancelRequestRequester(requesterId, requestId);
    }

    @GetMapping("/requests")
    public List<RequestDto> getRequestRequester(
            @Positive @PathVariable(name = "userId") Long requesterId
    ) {
        log.info("Get all requests for requester with requesterId={}", requesterId);
        return service.getRequestRequester(requesterId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<RequestDto> getRequestInitiator(
            @Positive @PathVariable(name = "userId") Long initiatorId,
            @Positive @PathVariable(name = "eventId") Long eventId
    ) {
        log.info("Get all requests for requester with initiatorId={}, eventId={}", initiatorId, eventId);
        return service.getRequestInitiator(initiatorId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResultDto updateRequestStatusInitiator(
            @Positive @PathVariable(name = "userId") Long initiatorId,
            @Positive @PathVariable(name = "eventId") Long eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequest requestInitiatorDto
    ) {
        log.info("Update request status with initiatorId={}, eventId={}", initiatorId, eventId);
        return service.updateRequestStatusInitiator(initiatorId, eventId, requestInitiatorDto);
    }
}
