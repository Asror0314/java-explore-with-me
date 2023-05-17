package ru.yandex.explore.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.explore.category.dto.CategoryDto;
import ru.yandex.explore.location.dto.LocationDto;
import ru.yandex.explore.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventFullDto {
    private Long id;
    private String annotation; //
    private CategoryDto category; //
    private int confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn; //
    private String description; //
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate; //
    private UserShortDto initiator; //
    private LocationDto location; //
    private boolean paid; //
    private int participantLimit; //
    private LocalDateTime publishedOn;
    private boolean requestModeration; //
    private EventState state; //
    private String title; //
    private int views;
}
