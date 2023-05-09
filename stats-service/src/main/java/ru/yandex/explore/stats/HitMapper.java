package ru.yandex.explore.stats;

import ru.yandex.explore.stats.dto.HitDto;
import ru.yandex.explore.stats.model.Hit;

public class HitMapper {
    public static Hit mapHit(HitDto hitDto) {
        final Hit hit = new Hit();

        hit.setApp(hitDto.getApp());
        hit.setUri(hitDto.getUri());
        hit.setIp(hitDto.getIp());
        hit.setTimestamp(hitDto.getTimestamp());
        return hit;
    }

    public static HitDto mapHitDto(Hit hit) {
        final HitDto hitDto = new HitDto();

        hitDto.setId(hit.getId());
        hitDto.setApp(hit.getApp());
        hitDto.setUri(hit.getUri());
        hitDto.setIp(hit.getIp());
        hitDto.setTimestamp(hit.getTimestamp());
        return hitDto;
    }
}
