package ru.yandex.explore.stats.hit;

import ru.yandex.explore.stats.dto.HitDto;
import ru.yandex.explore.stats.dto.NewHitDto;
import ru.yandex.explore.stats.hit.model.Hit;

public class HitMapper {
    public static Hit mapToHit(NewHitDto hitDto) {
        final Hit hit = new Hit();

        hit.setApp(hitDto.getApp());
        hit.setUri(hitDto.getUri());
        hit.setIp(hitDto.getIp());
        hit.setTimestamp(hitDto.getTimestamp());
        return hit;
    }

    public static HitDto mapToHitDto(Hit hit) {
        final HitDto hitDto = new HitDto();

        hitDto.setId(hit.getId());
        hitDto.setApp(hit.getApp());
        hitDto.setUri(hit.getUri());
        hitDto.setIp(hit.getIp());
        hitDto.setTimestamp(hit.getTimestamp());
        return hitDto;
    }
}
