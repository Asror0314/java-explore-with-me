package ru.yandex.explore.stats.hit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.explore.stats.dto.HitDto;
import ru.yandex.explore.stats.dto.NewHitDto;
import ru.yandex.explore.stats.dto.StatsDto;
import ru.yandex.explore.stats.hit.error.ConstraintViolationException;
import ru.yandex.explore.stats.hit.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class HitServiceImpl implements HitService {

    @Autowired
    private HitRepository repository;

    @Override
    public HitDto addNewHit(NewHitDto hitDto) {
        final Hit savedHit = repository.save(HitMapper.map2Hit(hitDto));
        return HitMapper.map2HitDto(savedHit);
    }

    @Override
    public List<StatsDto> getStats(String start, String end, List<String> uris, boolean unique) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        final LocalDateTime endDate = LocalDateTime.parse(end, formatter);

        if (startDate.isAfter(endDate)) {
            throw new ConstraintViolationException("The start date must be earlier than the end date");
        }

        if (uris == null) {
            if (unique) {
                return repository.getUniqueStatsWithoutUri(startDate, endDate);
            } else {
                return repository.getStatsWithoutUri(startDate, endDate);
            }
        } else {
            if (unique) {
                return repository.getUniqueStats(startDate, endDate, uris);
            } else {
                return repository.getStats(startDate, endDate, uris);
            }
        }
    }
}
