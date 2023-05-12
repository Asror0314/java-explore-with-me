package ru.yandex.explore.hit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.explore.stats.dto.HitDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Validated
public class HitController {

    @Autowired
    private HitService client;

    @PostMapping("/hit")
    public ResponseEntity<Object> addNewHit(@Valid @RequestBody HitDto hitDto) {
        return client.addNewHit(hitDto);
    }

    @GetMapping("stats")
    public ResponseEntity<Object> getStats(
            @RequestParam(name = "start")
            String start,
            @RequestParam(name = "end")
            String end,
            @RequestParam(name = "uris", required = false) List<String> uris,
            @RequestParam(name = "unique", defaultValue = "false") boolean unique
    ) {
        log.info("Get stats with start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        return client.getStats(start, end, uris, unique);
    }
}
