package ru.yandex.explore.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.explore.request.dto.ParticipationRequestDto;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class ParticipationRequestController {
    private final ParticipationRequestService service;

    @PostMapping
    public ParticipationRequestDto addNewRequest(
            @PathVariable(name = "userId") Long requesterId,
            @RequestParam(name = "eventId") Long eventId
    ) {
        log.info("Creating new participation request with userId={}, eventId={}", requesterId, eventId);
        return service.addNewRequest(requesterId, eventId);
    }
}
