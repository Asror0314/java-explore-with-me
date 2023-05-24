package ru.yandex.explore.compilation.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class NewCompilationDto {
    private Set<Long> events;
    private boolean pinned;
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String title;
}
