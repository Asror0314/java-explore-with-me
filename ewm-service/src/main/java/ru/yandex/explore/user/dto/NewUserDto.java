package ru.yandex.explore.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class NewUserDto {
    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

}
