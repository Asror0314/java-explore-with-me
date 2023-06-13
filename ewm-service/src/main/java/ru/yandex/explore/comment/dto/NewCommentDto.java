package ru.yandex.explore.comment.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class NewCommentDto {
    @NotNull
    @NotBlank
    private String text;
}
