package ru.yandex.explore.compilation.dto;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.explore.event.dto.EventShortDto;
import java.util.List;

@Getter
@Setter
public class CompilationDto {
    private Long id;

    private List<EventShortDto> events;

    private boolean pinned;

    private String title;
}
