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
import ru.yandex.explore.location.dto.LocationDto;
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
    private final LocalDateTime nowDateTime = LocalDateTime.now();

    @Override
    public EventFullDto addNewEvent(NewEventDto newEventDto, Long initiatorId) {
        final User initiator = findUserById(initiatorId);
        validEventDate(2, newEventDto.getEventDate());

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
    public EventFullDto updateEventUser(UpdateEventUserDto eventDto, Long initiatorId, Long eventId) {
        findUserById(initiatorId);
        final Event event = findEventById(eventId);
        final EventState eventState = validUpdateEventUser(event, eventDto);
        event.setState(eventState);
        validLocation(event.getLocation(), eventDto.getLocation());

        final Category category;
        if (eventDto.getCategory() != null) {
            category = findCategoryById(eventDto.getCategory());
        } else {
            category = event.getCategory();
        }

        eventRepository.updateEventUser(eventId, eventDto.getAnnotation(), category, nowDateTime, eventDto.getDescription(),
                eventDto.getEventDate(), event.getLocation(), eventDto.getPaid(), eventDto.getParticipantLimit(), eventDto.getRequestModeration(),
                eventState, eventDto.getTitle());

        final Event savedEvent = eventRepository.findById(eventId).orElseThrow();

        return EventMapper.mapEvent2EventFullDto(savedEvent);


    }

    @Override
    public EventFullDto getEventUserById(Long initiatorId, Long eventId) {
        findUserById(initiatorId);
        final Event event = findEventById(eventId);

        return EventMapper.mapEvent2EventFullDto(event);
    }

    @Override
    public EventFullDto updateEventAdmin(UpdateEventAdminDto eventDto, Long eventId) {
        final Event event = findEventById(eventId);
        final Location location = event.getLocation();
        final EventState updateState = validUpdateEventAdmin(event, eventDto);
        validLocation(location, eventDto.getLocation());

        final Category category;
        if (eventDto.getCategory() != 0) {
            category = findCategoryById(eventDto.getCategory());
        } else {
            category = event.getCategory();
        }

        eventRepository
                    .updateEventAdmin(eventId, eventDto.getAnnotation(), category, nowDateTime, eventDto.getDescription(),
                            eventDto.getEventDate(), location, eventDto.getPaid(), eventDto.getParticipantLimit(),
                            eventDto.getRequestModeration(), nowDateTime, updateState, eventDto.getTitle());

        final Event updatedEvent = eventRepository.findById(eventId).get();
        return EventMapper.mapEvent2EventFullDto(updatedEvent);
    }

    @Override
    public List<EventFullDto> getEventsAdmin(
            Set<Long> users,
            Set<String> states,
            Set<Long> categories,
            String rangeStart,
            String rangeEnd,
            int from,
            int size
    ) {
        final List<LocalDateTime> requestDateTime = validRequestDateTime(rangeStart, rangeEnd);

        final List<Event> events =
                eventRepository.findAllAdmin(users, states, categories, requestDateTime.get(0), requestDateTime.get(1), from, size);
        return events
                .stream()
                .map(EventMapper::mapEvent2EventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getPublishEventById(Long eventId) {
        final Event event = findEventById(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException(String.format("Event with id = %d was not found!", eventId));
        }

        return EventMapper.mapEvent2EventFullDto(event);
    }

    @Override
    public List<EventShortDto> getPublishEvents(
            String text,
            Set<Long> categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            Boolean onlyAvailable,
            String sort,
            int from,
            int size
    ) {
        final List<LocalDateTime> requestDateTime = validRequestDateTime(rangeStart, rangeEnd);
        final List<Event> events = eventRepository.findAllPublishWithSortEventDate(text, categories, paid,
                                requestDateTime.get(0), requestDateTime.get(1), from, size);
        return events
                .stream()
                .map(EventMapper::mapEvent2EventShortDto)
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

    private EventState validUpdateEventAdmin(Event event, UpdateEventAdminDto eventDto) {
        EventState updateState = EventState.PENDING;
        validEventDate(1, event.getEventDate());

        switch (eventDto.getStateAction()) {
            case CANCEL_REVIEW: {
                if (event.getState().equals(EventState.PUBLISHED)) {
                    throw new EditRulesException(String.format("Cannot cancel the event because it's not in the right state: %s", event.getState()));
                }
                updateState = EventState.CANCELED;
                break;
            }
            case PUBLISH_EVENT: {
                if (!event.getState().equals(EventState.PENDING)) {
                    throw new EditRulesException(String.format("Cannot publish the event because it's not in the right state: %s", event.getState()));
                }
                updateState = EventState.PUBLISHED;
                break;
            }
        }
        return updateState;
    }

    private EventState validUpdateEventUser(Event event, UpdateEventUserDto updateEventDto) {
        validEventDate(2, event.getEventDate());

        if (event.getState().equals(EventState.REJECTED)
                || event.getState().equals(EventState.PENDING)) {
            switch (updateEventDto.getStateAction()) {
                case CANCEL_REVIEW: {
                    return EventState.CANCELED;
                }
                case PUBLISH_EVENT: return EventState.PENDING;
                default: return event.getState();
            }
        } else {
            throw new EditRulesException("Only pending or canceled events can be changed");
        }
    }

    private void validLocation(Location location, LocationDto locationDto) {
        double newLat = location.getLat();
        double newLon = location.getLon();
        if (location.getLat() != newLat
                || location.getLon() != newLon) {
            location.setLat(newLat);
            location.setLon(newLon);
        }
    }

    private void validEventDate(int hour, LocalDateTime dateTime) {
        if (dateTime.isBefore((nowDateTime.plusHours(hour)))) {
            throw new EditRulesException(String.format("Field: eventDate. Error: должно содержать дату, " +
                    "которая еще не наступила. Value: %s", dateTime));
        }
    }

    private List<LocalDateTime> validRequestDateTime(String rangeStart, String rangeEnd) {
        LocalDateTime start;
        LocalDateTime end;

        if (rangeStart == null) {
            start = nowDateTime;
            end = LocalDateTime.MAX;
        } else {
            start = LocalDateTime.parse(rangeStart, formatter);
            end = LocalDateTime.parse(rangeEnd, formatter);
        }
        return List.of(start, end);
    }
}
