package ru.yandex.explore.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.explore.category.Category;
import ru.yandex.explore.category.CategoryService;
import ru.yandex.explore.event.dto.*;
import ru.yandex.explore.exception.ConstraintViolationException;
import ru.yandex.explore.exception.EditRulesException;
import ru.yandex.explore.exception.NotFoundException;
import ru.yandex.explore.location.Location;
import ru.yandex.explore.location.LocationService;
import ru.yandex.explore.stats.client.StatsClient;
import ru.yandex.explore.stats.dto.StatsDto;
import ru.yandex.explore.user.User;
import ru.yandex.explore.user.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final UserService userService;
    private final CategoryService catService;
    private final StatsClient statsClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String datetime = LocalDateTime.now().format(formatter);
    private final LocalDateTime createdDate = LocalDateTime.parse(datetime, formatter);

    @Override
    public EventFullDto addNewEvent(NewEventDto newEventDto, Long initiatorId) {
        final User initiator = userService.findUserById(initiatorId);
        validEventDate(2, newEventDto.getEventDate());

        if (newEventDto.getRequestModeration() == null) {
            newEventDto.setRequestModeration(true);
        }

        final Long catId = newEventDto.getCategory();
        final Category category = catService.findCategoryById(catId);
        final Location location = locationService.addNewLocation(newEventDto.getLocation());
        final Event event = EventMapper.mapNewEventDtoToEvent(newEventDto, initiator, category, location);

        final Event addedEvent = eventRepository.save(event);

        return EventMapper.mapEventToEventFullDto(addedEvent);
    }

    @Override
    public List<EventShortDto> getEventsUser(Long initiatorId, int from, int size) {
        userService.findUserById(initiatorId);
        final List<Event> events = eventRepository.findAllByInitiator(initiatorId, from, size);

        return events.stream().map(EventMapper::mapEventToEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventUser(UpdateEventUserDto eventDto, Long initiatorId, Long eventId) {
        userService.findUserById(initiatorId);
        final Event event = findEventById(eventId);
        final EventState eventState = validUpdateEventUser(event, eventDto);
        event.setState(eventState);
        validLocation(event.getLocation());

        final Category category;
        if (eventDto.getCategory() != null) {
            category = catService.findCategoryById(eventDto.getCategory());
        } else {
            category = event.getCategory();
        }

        eventRepository.updateEventUser(eventId, eventDto.getAnnotation(), category, createdDate, eventDto.getDescription(),
                eventDto.getEventDate(), event.getLocation(), eventDto.getPaid(), eventDto.getParticipantLimit(), eventDto.getRequestModeration(),
                eventState, eventDto.getTitle());

        final Event savedEvent = findEventById(eventId);

        return EventMapper.mapEventToEventFullDto(savedEvent);
    }

    @Override
    public EventFullDto getEventUserById(Long initiatorId, Long eventId) {
        userService.findUserById(initiatorId);
        final Event event = findEventById(eventId);

        return EventMapper.mapEventToEventFullDto(event);
    }

    @Override
    public EventFullDto updateEventAdmin(UpdateEventAdminDto eventDto, Long eventId) {
        final Event event = findEventById(eventId);
        final Location location = event.getLocation();
        final EventState updateState = validUpdateEventAdmin(event, eventDto);
        validLocation(location);

        final Category category;
        if (eventDto.getCategory() != null) {
            category = catService.findCategoryById(eventDto.getCategory());
        } else {
            category = event.getCategory();
        }

        eventRepository
                    .updateEventAdmin(eventId, eventDto.getAnnotation(), category, createdDate, eventDto.getDescription(),
                            eventDto.getEventDate(), location, eventDto.getPaid(), eventDto.getParticipantLimit(),
                            eventDto.getRequestModeration(), createdDate, updateState, eventDto.getTitle());

        final Event updatedEvent = findEventById(eventId);
        return EventMapper.mapEventToEventFullDto(updatedEvent);
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
        final LocalDateTime startDate = validRequestDateTime(rangeStart);
        final LocalDateTime endDate = validRequestDateTime(rangeEnd);

        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                throw new ConstraintViolationException("The start date must be earlier than the end date");
            }
        }

        users = validLongFieldIsNull(users);
        states = validStringFieldIsNull(states);
        categories = validLongFieldIsNull(categories);

        final List<Event> events =
                eventRepository.findAllAdmin(users, states, categories,
                        startDate, endDate, from, size);
        return events
                .stream()
                .map(EventMapper::mapEventToEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getPublishEventById(Long eventId) {
        final Event event = findEventById(eventId);

        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new NotFoundException(String.format("Event with id = %d was not found!", eventId));
        }
        setCountHits(event);

        return EventMapper.mapEventToEventFullDto(event);
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
        final LocalDateTime startDate = validRequestDateTime(rangeStart);
        final LocalDateTime endDate = validRequestDateTime(rangeEnd);
        categories = validLongFieldIsNull(categories);

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new ConstraintViolationException("The start date must be earlier than the end date");
        }

        final List<Event> events = eventRepository.findAllPublishWithSortEventDate(text, categories, paid,
                                startDate, endDate, from, size);
        events.forEach(event -> setCountHits(event));

        return events
                .stream()
                .map(EventMapper::mapEventToEventShortDto)
                .collect(Collectors.toList());
    }

    private Set<Long> validLongFieldIsNull(Set<Long> field) {
        if (field == null) {
            field = new HashSet<>();
            field.add(0L);
        }
        return field;
    }

    private Set<String> validStringFieldIsNull(Set<String> field) {
        if (field == null) {
            field = new HashSet<>();
            field.add("");
        }
        return field;
    }

    private void setCountHits(Event event) {
        final String uriEventId = String.format("/events/%d", event.getId());
        final List<String> uris = Arrays.asList(uriEventId);
        final Object statsBody = statsClient
                .getStats(LocalDateTime.now().minusYears(2).format(formatter), datetime, uris, true)
                .getBody();

        List<StatsDto> statsDto = new ObjectMapper().convertValue(statsBody, new TypeReference<>() {
        });

        statsDto.forEach(stats -> {
            if (uriEventId.equals(stats.getUri())) {
                event.setViews(stats.getHits());
            }
        });
    }

    @Override
    public Event findEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Event with id = %d was not found", eventId)));
    }

    private EventState validUpdateEventAdmin(Event event, UpdateEventAdminDto eventDto) {
        validEventDate(1, event.getEventDate());

        if (eventDto.getStateAction() == null) {
            return event.getState();
        }

        switch (eventDto.getStateAction()) {
            case REJECT_EVENT: {
                if (EventState.PUBLISHED.equals(event.getState())) {
                    throw new EditRulesException(String.format("Cannot cancel the event because it's not in " +
                            "the right state: %s", event.getState()));
                }
                return EventState.REJECTED;
            }
            case PUBLISH_EVENT: {
                if (!EventState.PENDING.equals(event.getState())) {
                    throw new EditRulesException(String.format("Cannot publish the event because it's not in " +
                            "the right state: %s", event.getState()));
                }
                return EventState.PUBLISHED;
            }
            default: {
                throw new ConstraintViolationException(
                        String.format("Unknown stateAction: %s", eventDto.getStateAction()));
            }
        }
    }

    private EventState validUpdateEventUser(Event event, UpdateEventUserDto eventDto) {
        validEventDate(2, event.getEventDate());

        if (EventState.REJECTED.equals(event.getState())
                || EventState.PENDING.equals(event.getState())) {
            if (eventDto.getStateAction() == null) {
                return event.getState();
            }

            switch (eventDto.getStateAction()) {
                case CANCEL_REVIEW: {
                    return EventState.CANCELED;
                }
                case SEND_TO_REVIEW: {
                    return EventState.PENDING;
                }
                default: {
                    throw new ConstraintViolationException(
                            String.format("Unknown stateAction: %s", eventDto.getStateAction()));
                }
            }
        } else {
            throw new EditRulesException("Only pending or canceled events can be changed");
        }
    }

    private void validLocation(Location location) {
        double newLat = location.getLat();
        double newLon = location.getLon();
        if (location.getLat() != newLat
                || location.getLon() != newLon) {
            location.setLat(newLat);
            location.setLon(newLon);
        }
    }

    private void validEventDate(int hour, LocalDateTime dateTime) {
        if (dateTime.isBefore((createdDate.plusHours(hour)))) {
            throw new EditRulesException(String.format("Field: eventDate. Error: должно содержать дату, " +
                    "которая еще не наступила. Value: %s", dateTime));
        }
    }

    private LocalDateTime validRequestDateTime(String dateTime) {

        if (dateTime == null) {
            return null;
        } else {
            return LocalDateTime.parse(dateTime, formatter);
        }
    }
}
