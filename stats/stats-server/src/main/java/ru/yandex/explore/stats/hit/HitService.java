package ru.yandex.explore.stats.hit;

import ru.yandex.explore.stats.dto.HitDto;
import ru.yandex.explore.stats.dto.NewHitDto;
import ru.yandex.explore.stats.dto.StatsDto;

import java.util.List;

public interface HitService {
    HitDto addNewHit(NewHitDto hitDto);

    List<StatsDto> getStats(String start, String end, List<String> uris, boolean unique);
}
