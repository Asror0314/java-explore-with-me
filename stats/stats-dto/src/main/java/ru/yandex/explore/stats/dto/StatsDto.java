package ru.yandex.explore.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatsDto {
    String app;
    String uri;
    Long hits;
}
