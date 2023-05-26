package ru.yandex.explore.stats.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StatsDto {
    String app;
    String uri;
    Long hits;
}
