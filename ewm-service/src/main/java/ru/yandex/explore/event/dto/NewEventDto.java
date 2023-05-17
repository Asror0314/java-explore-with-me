package ru.yandex.explore.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.explore.location.dto.LocationDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class NewEventDto {
    @NotNull
    private String annotation;
    @NotNull
    private Long category;
    @NotNull
    private String description;
    @NotNull
    @Future(message = "должно содержать дату, которая еще не наступила")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private LocationDto location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    @NotNull
    private String title;
}
