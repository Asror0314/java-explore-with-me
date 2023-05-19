package ru.yandex.explore.category.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateCategoryDto {
    @NotNull
    private String name;
}
