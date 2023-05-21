package ru.yandex.explore.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.explore.event.dto.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@Transactional(readOnly = true)
public class EventController {
    private final EventService service;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public EventFullDto addNewEvent(
            @Valid @RequestBody NewEventDto newEventDto,
            @Positive @PathVariable(name = "userId") Long initiatorId
            ) {
        log.info("Creating new event with userId = {}", initiatorId);
        return service.addNewEvent(newEventDto, initiatorId);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEventUser(
            @Positive @PathVariable(name = "userId") Long initiatorId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        log.info("Get events with userId={}");
        return service.getEventsUser(initiatorId, from, size);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    @Transactional
    public EventFullDto updateEventUser(
            @RequestBody UpdateEventUserDto eventDto,
            @Positive @PathVariable(name = "userId") Long initiatorId,
            @Positive @PathVariable(name = "eventId") Long eventId
    ) {
        log.info("updating event with userId={}, eventId={}", initiatorId, eventId);
        return service.updateEventUser(eventDto, initiatorId, eventId);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEventUserById(
            @Positive @PathVariable(name = "userId") Long initiatorId,
            @Positive @PathVariable(name = "eventId") Long eventId
    ) {
        log.info("Get event with userId={}, eventId={}", initiatorId, eventId);
        return service.getEventUserById(initiatorId, eventId);
    }

    @PatchMapping("/admin/events/{eventId}")
    @Transactional
    public EventFullDto updateEventAdmin(
            @RequestBody UpdateEventAdminDto eventDto,
            @Positive @PathVariable(name = "eventId") Long eventId
    ) {
        log.info("updating event with eventId={}", eventId);
        return service.updateEventAdmin(eventDto, eventId);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> getEventsAdmin(
            @RequestParam(name = "users", required = false) Set<Long> users,
            @RequestParam(name = "states", required = false) Set<String> states,
            @RequestParam(name = "categories", required = false) Set<Long> categories,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        log.info("Get events for admin with users={}, states={}, categories={}, rangeStart={}, " +
                "rangeEnd={}, from={}, size={}", users, states, categories, rangeStart, rangeEnd, from, size);
        return service.getEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getPublishEventById(@Positive @PathVariable(name = "id") Long eventId) {
        log.info("Get event with id={}", eventId);
        return service.getPublishEventById(eventId);
    }

    @GetMapping("/events")
    public List<EventShortDto> getPublishEvents(
        @RequestParam(name = "text", required = false) String text,
        @RequestParam(name = "categories", required = false) Set<Long> categories,
        @RequestParam(name = "paid", required = false) Boolean paid,
        @RequestParam(name = "rangeStart", required = false) String rangeStart,
        @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
        @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
        @RequestParam(name = "sort", required = false) String sort,
        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
        @Positive @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        log.info("Get publish events with taxt={}, categories={}, paid={}, rangeStart={}, rangeEnd={}, " +
                "onlyAvailable={}, sort={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return service.getPublishEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }
}
