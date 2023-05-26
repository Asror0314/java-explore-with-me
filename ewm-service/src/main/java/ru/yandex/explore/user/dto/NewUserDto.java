package ru.yandex.explore.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class NewUserDto {
    @NotNull
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
    @NotNull
    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    private String email;

}
