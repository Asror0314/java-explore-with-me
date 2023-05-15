package ru.yandex.explore.event.dto;

import ru.yandex.explore.category.dto.CategoryDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class NewEventDto {
    @NotNull
    private String annotation;

    @NotNull
    private CategoryDto category;

    @NotNull
    private String description;

    @NotNull
    private LocalDateTime eventDate;

    @NotNull
    private LocationDto location;

    private boolean paid;

    private int participantLimit;

    private boolean requestModeration;

    @NotNull
    private String title;
}
