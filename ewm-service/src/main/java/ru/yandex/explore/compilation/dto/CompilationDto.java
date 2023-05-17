package ru.yandex.explore.compilation.dto;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.explore.event.Event;

import java.util.Set;

@Getter
@Setter
public class CompilationDto {
    private Long id;

    private Set<Long> events;

    private boolean pinned;

    private String title;
}
