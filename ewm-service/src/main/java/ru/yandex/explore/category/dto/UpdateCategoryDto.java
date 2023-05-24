package ru.yandex.explore.category.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateCategoryDto {
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String name;
}
