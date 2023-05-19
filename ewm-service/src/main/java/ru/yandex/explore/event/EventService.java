package ru.yandex.explore.event;

import ru.yandex.explore.event.dto.*;

import java.util.List;
import java.util.Set;

public interface EventService {
    EventFullDto addNewEvent(NewEventDto newEventDto, Long initiatorId);

    List<EventShortDto> getEventsUser(Long initiatorId, int from, int size);

    EventFullDto updateEventUser(UpdateEventUserDto updateEventDto, Long initiatorId, Long eventId);

    EventFullDto getEventUserById(Long initiatorId, Long eventId);

    EventFullDto updateEventAdmin(UpdateEventAdminDto updateEventDto, Long eventId);

    List<EventFullDto> getEventsAdmin(Set<Integer> users, Set<String> states, Set<Integer> categories,
                                      String rangeStart, String rangeEnd, Integer from, Integer size);

}
