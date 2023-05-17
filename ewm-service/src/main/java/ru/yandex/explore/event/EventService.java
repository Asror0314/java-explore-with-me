package ru.yandex.explore.event;

import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.explore.event.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventService {
    EventFullDto addNewEvent(NewEventDto newEventDto, Long initiatorId);

    EventFullDto updateEventUser(UpdateEventUserRequest updateEventDto, Long initiatorId, Long eventId);

    EventFullDto updateEventAdmin(UpdateEventAdminRequest updateEventDto, Long eventId);

    List<EventFullDto> getEventsAdmin(Set<Integer> users, Set<String> states, Set<Integer> categories,
                                      String rangeStart, String rangeEnd, Integer from, Integer size);

    List<EventShortDto> getEventsUser(Long initiatorId, int from, int size);

    EventFullDto getEventUserById(Long initiatorId, Long eventId);

}
