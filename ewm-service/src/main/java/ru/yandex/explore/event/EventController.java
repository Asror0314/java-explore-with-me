package ru.yandex.explore.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.explore.event.dto.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventController {
    private final EventService service;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addNewEvent(
            @Valid @RequestBody NewEventDto newEventDto,
            @PathVariable(name = "userId") Long initiatorId
            ) {
        log.info("Creating new event with userId = {}", initiatorId);
        return service.addNewEvent(newEventDto, initiatorId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus()
    public EventFullDto updateEventUser(
            @RequestBody UpdateEventUserRequest eventDto,
            @PathVariable(name = "userId") Long initiatorId,
            @PathVariable(name = "eventId") Long eventId
    ) {
        log.info("updating event with userId={}, eventId={}", initiatorId, eventId);
        return service.updateEventUser(eventDto, initiatorId, eventId);
    }

    @PatchMapping("/admin/events/{eventId}")
    @ResponseStatus()
    public EventFullDto updateEventAdmin(
            @RequestBody UpdateEventAdminRequest eventDto,
            @PathVariable(name = "eventId") Long eventId
    ) {
        log.info("updating event with eventId={}", eventId);
        return service.updateEventAdmin(eventDto, eventId);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> getEventsAdmin(
            @RequestParam(name = "users") Set<Integer> users,
            @RequestParam(name = "states") Set<String> states,
            @RequestParam(name = "categories") Set<Integer> categories,
            @RequestParam(name = "rangeStart")
            String rangeStart,
            @RequestParam(name = "rangeEnd")
            String rangeEnd,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        log.info("Get events for admin with users={}, states={}, categories={}, rangeStart={}, " +
                "rangeEnd={}, from={}, size={}", users, states, categories, rangeStart, rangeEnd, from, size);
        return service.getEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEventUser(
            @PathVariable(name = "userId") Long initiatorId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        log.info("Get events with userId={}");
        return service.getEventsUser(initiatorId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEventUserById(
            @PathVariable(name = "userId") Long initiatorId,
            @PathVariable(name = "eventId") Long eventId
    ) {
        log.info("Get event with userId={}, eventId={}", initiatorId, eventId);
        return service.getEventUserById(initiatorId, eventId);
    }

}
