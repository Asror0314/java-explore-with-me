package ru.yandex.explore.stats.hit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.explore.stats.dto.HitDto;
import ru.yandex.explore.stats.dto.NewHitDto;
import ru.yandex.explore.stats.dto.StatsDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HitController {

    private final HitService service;

    @PostMapping("/hit")
    @Transactional
    public HitDto addNewHit(@Valid @RequestBody NewHitDto hitDto) {
        log.info("Creating new hit");
        return service.addNewHit(hitDto);
    }

    @GetMapping("stats")
    public List<StatsDto> getStats(
            @RequestParam(name = "start")
            String start,
            @RequestParam(name = "end")
            String end,
            @RequestParam(name = "uris", required = false) List<String> uris,
            @RequestParam(name = "unique", defaultValue = "false") boolean unique
            ) {
        log.info("Get stats with start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        List<StatsDto> stats = service.getStats(start, end, uris, unique);
        return stats;
    }
}
