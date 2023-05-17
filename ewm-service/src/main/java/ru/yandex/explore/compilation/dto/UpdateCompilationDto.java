package ru.yandex.explore.compilation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateCompilationDto {
    private Set<Long> events;
    private boolean pinned;
    private String title;
}
