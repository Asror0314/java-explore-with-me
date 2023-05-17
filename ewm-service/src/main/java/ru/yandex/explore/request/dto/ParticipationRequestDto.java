package ru.yandex.explore.request.dto;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.explore.event.dto.EventState;

import java.time.LocalDateTime;

@Getter
@Setter
public class ParticipationRequestDto {
    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private EventState status;
}
