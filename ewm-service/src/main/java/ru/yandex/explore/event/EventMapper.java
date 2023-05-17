package ru.yandex.explore.event;

import ru.yandex.explore.category.Category;
import ru.yandex.explore.category.CategoryMapper;
import ru.yandex.explore.category.dto.CategoryDto;
import ru.yandex.explore.event.dto.EventFullDto;
import ru.yandex.explore.event.dto.EventShortDto;
import ru.yandex.explore.event.dto.EventState;
import ru.yandex.explore.event.dto.NewEventDto;
import ru.yandex.explore.location.Location;
import ru.yandex.explore.location.LocationMapper;
import ru.yandex.explore.location.dto.LocationDto;
import ru.yandex.explore.user.User;
import ru.yandex.explore.user.UserMapper;
import ru.yandex.explore.user.dto.UserShortDto;

import java.time.LocalDateTime;

public class EventMapper {
    public static Event mapNewEventDto2Event(
            NewEventDto newEventDto,
            User initiator,
            Category category,
            Location location
    ) {
        final Event event = new Event();

        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setInitiator(initiator);
        event.setLocation(location);
        event.setPaid(newEventDto.isPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.isRequestModeration());
        event.setState(EventState.PENDING);
        event.setTitle(newEventDto.getTitle());

        return event;
    }

    public static EventFullDto mapEvent2EventFullDto(Event event) {
        final EventFullDto eventFullDto = new EventFullDto();
        final UserShortDto initiator = UserMapper.mapUser2UserShortDto(event.getInitiator());
        final CategoryDto category = CategoryMapper.mapCategory2CategoryDto(event.getCategory());
        final LocationDto locationDto = LocationMapper.mapLocation2LocationDto(event.getLocation());

        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(category);
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setInitiator(initiator);
        eventFullDto.setLocation(locationDto);
        eventFullDto.setPaid(event.isPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setRequestModeration(event.isRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());

        return eventFullDto;
    }

    public static EventShortDto mapEvent2EventShortDto(Event event) {
        final EventShortDto eventShortDto = new EventShortDto();
        final UserShortDto initiator = UserMapper.mapUser2UserShortDto(event.getInitiator());
        final CategoryDto category = CategoryMapper.mapCategory2CategoryDto(event.getCategory());

        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(category);
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setInitiator(initiator);
        eventShortDto.setPaid(event.isPaid());
        eventShortDto.setTitle(event.getTitle());

        return eventShortDto;
    }
}