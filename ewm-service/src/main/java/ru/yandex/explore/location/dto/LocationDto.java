package ru.yandex.explore.location.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LocationDto {
    @NotNull
    private double lat;
    @NotNull
    private double lon;
}
