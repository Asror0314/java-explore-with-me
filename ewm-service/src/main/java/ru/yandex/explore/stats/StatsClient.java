package ru.yandex.explore.stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.yandex.explore.stats.client.BaseClient;
import ru.yandex.explore.stats.dto.HitDto;

import java.util.List;
import java.util.Map;

public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addNewHit(HitDto hitDto) {
        return post("/hit", hitDto);
    }

    public ResponseEntity<Object> getStats(String start, String end, List<String> uris, boolean unique) {
        final StringBuilder urisBuild = new StringBuilder();

       if (uris != null) {
            for (String uri : uris) {
                urisBuild.append("&uris=").append(uri);
            }
        }
        urisBuild.delete(0,6);
        final Map<String, Object> parametres = Map.of(
                "start", start,
                "end", end,
                "uris", urisBuild,
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parametres);
    }
}
