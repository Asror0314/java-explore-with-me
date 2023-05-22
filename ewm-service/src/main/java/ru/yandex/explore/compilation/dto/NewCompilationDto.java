package ru.yandex.explore.compilation.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class NewCompilationDto {
    @NotNull
    private Set<Long> events;
    private Boolean pinned;
    @NotNull
    @NotBlank
    private String title;
}
