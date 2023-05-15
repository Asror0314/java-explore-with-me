package ru.yandex.explore.event.dto;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.explore.category.dto.CategoryDto;
import ru.yandex.explore.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventFullDto {
    private Long id;

    private String annotation;

    private CategoryDto category;

    private int confirmedRequests;

    private LocalDateTime createdOn;

    private String description;

    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private LocationDto location;

    private boolean paid;

    private int participantLimit;

    private LocalDateTime publishedOn;

    private boolean requestModeration;

    private String state;

    private String title;

    private int views;
}
