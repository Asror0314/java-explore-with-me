package ru.yandex.explore.compilation.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class UpdateCompilationDto {
    private Set<Long> events;
    private boolean pinned;
    @Size(max = 50)
    private String title;
}
