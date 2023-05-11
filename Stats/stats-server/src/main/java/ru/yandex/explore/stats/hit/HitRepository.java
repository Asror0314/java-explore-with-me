package ru.yandex.explore.stats.hit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yandex.explore.stats.dto.StatsDto;
import ru.yandex.explore.stats.hit.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT new ru.yandex.explore.stats.dto.StatsDto( h.app, h.uri, COUNT(h.ip) ) " +
            "FROM Hit as h " +
            "WHERE h.timestamp >= ?1 AND h.timestamp <= ?2 " +
            "AND h.uri in ?3 " +
            "GROUP by h.uri, h.app ORDER BY COUNT(h.ip) DESC")
    List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.yandex.explore.stats.dto.StatsDto( h.app, h.uri, COUNT(h.ip) ) " +
            "FROM Hit as h " +
            "WHERE h.timestamp >= ?1 AND h.timestamp <= ?2 " +
            "GROUP by h.uri, h.app ORDER BY COUNT(h.ip) DESC ")
    List<StatsDto> getStatsWithoutUri(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.yandex.explore.stats.dto.StatsDto( h.app, h.uri, COUNT(distinct h.ip) ) " +
            "FROM Hit as h " +
            "WHERE h.timestamp >= ?1 AND h.timestamp <= ?2 " +
            "AND h.uri in ?3 " +
            "GROUP by h.uri, h.app ORDER BY COUNT(distinct h.ip) DESC")
    List<StatsDto> getUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.yandex.explore.stats.dto.StatsDto( h.app, h.uri, COUNT(distinct h.ip) ) " +
            "FROM Hit as h " +
            "WHERE h.timestamp >= ?1 AND h.timestamp <= ?2 " +
            "GROUP by h.uri, h.app ORDER BY COUNT(distinct h.ip) DESC ")
    List<StatsDto> getUniqueStatsWithoutUri(LocalDateTime start, LocalDateTime end);

}
