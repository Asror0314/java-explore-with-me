package ru.yandex.explore.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.explore.category.Category;
import ru.yandex.explore.category.CategoryRepository;
import ru.yandex.explore.event.dto.*;
import ru.yandex.explore.exception.EditRulesException;
import ru.yandex.explore.exception.NotFoundException;
import ru.yandex.explore.location.Location;
import ru.yandex.explore.location.LocationService;
import ru.yandex.explore.user.User;
import ru.yandex.explore.user.UserRepository;
import ru.yandex.explore.user.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository catRepository;
    private final LocationService locationService;
    private final UserService userService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EventFullDto addNewEvent(NewEventDto newEventDto, Long initiatorId) {
        final User initiator = findUserById(initiatorId);

        final Long catId = newEventDto.getCategory();
        final Category category = findCategoryById(catId);
        final Location location = locationService.addNewLocation(newEventDto.getLocation());

        final Event event = EventMapper.mapNewEventDto2Event(newEventDto, initiator, category, location);
        final Event addedEvent = eventRepository.save(event);

        return EventMapper.mapEvent2EventFullDto(addedEvent);
    }

    @Override
    public List<EventShortDto> getEventsUser(Long initiatorId, int from, int size) {
        userService.getUserById(initiatorId);
        final List<Event> events = eventRepository.findAllByInitiator(initiatorId, from, size);

        return events.stream().map(EventMapper::mapEvent2EventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventUser(UpdateEventUserDto updateEventDto, Long initiatorId, Long eventId) {
        final Event event = findEventById(eventId);

        if (event.getState().equals(EventState.REJECTED)
                || event.getState().equals(EventState.PENDING)) {
            switch (updateEventDto.getStateAction()) {
                case CANCEL_REVIEW: event.setState(EventState.REJECTED); break;
                case PUBLISH_EVENT: event.setState(EventState.PUBLISHED); break;
            }
            final Event updatedEvent = eventRepository.save(event);

            return EventMapper.mapEvent2EventFullDto(updatedEvent);
        } else {
             throw new EditRulesException("Only pending or canceled events can be changed");
        }


    }

    @Override
    public EventFullDto getEventUserById(Long initiatorId, Long eventId) {
        findUserById(initiatorId);
        final Event event = findEventById(eventId);

        return EventMapper.mapEvent2EventFullDto(event);
    }

    @Override
    public EventFullDto updateEventAdmin(UpdateEventAdminDto updateEventDto, Long eventId) {
        final Event event = findEventById(eventId);
        event.setState(updateEventDto.getStateAction());
        final Event updatedEvent = eventRepository.save(event);

        return EventMapper.mapEvent2EventFullDto(updatedEvent);
    }

    @Override
    public List<EventFullDto> getEventsAdmin(
            Set<Integer> users,
            Set<String> states,
            Set<Integer> categories,
            String rangeStart,
            String rangeEnd,
            Integer from,
            Integer size
    ) {

        final LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);
        final LocalDateTime end = LocalDateTime.parse(rangeEnd, formatter);

        final List<Event> events =
                eventRepository.findAllAdmin(users, states, categories, start, end, from, size);
        return events
                .stream()
                .map(EventMapper::mapEvent2EventFullDto)
                .collect(Collectors.toList());
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Event with id = %d was not found", eventId)));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("User with id = %d was not found", userId)));
    }

    private Category findCategoryById(Long catId) {
        return catRepository.findById(catId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Category with id = %d was not found", catId)));
    }
}
