package ru.yandex.explore.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDto {
    private Long id;
    private double lat;
    private double lon;
}
