package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query
            ("""
                    select new ru.practicum.ViewStatsDto(e.app, e.uri, count(e.ip))
                    from EndpointHit as e where e.timestamp >= :startTime and e.timestamp <= :endTime
                     and e.uri in :uris group by e.app, e.uri order by count(e.ip) desc
                    """)
    List<ViewStatsDto> getStats(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("uris") Set<String> uris);

    @Query
            ("""
                    select new ru.practicum.ViewStatsDto(e.app, e.uri, count(distinct e.ip))
                    from EndpointHit as e where e.timestamp >= :startTime and e.timestamp <= :endTime
                     and e.uri in :uris group by e.app, e.uri order by count(e.ip) desc
                    """)
    List<ViewStatsDto> getUniqueStats(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("uris") Set<String> uris);

    @Query
            ("""
                    select new ru.practicum.ViewStatsDto(e.app, e.uri, count(distinct e.ip))
                    from EndpointHit as e where e.timestamp >= :startTime and e.timestamp <= :endTime
                     group by e.app, e.uri order by count(e.ip) desc
                    """)
    List<ViewStatsDto> getUniqueStatsWithoutUris(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query
            ("""
                    select new ru.practicum.ViewStatsDto(e.app, e.uri, count(e.ip))
                    from EndpointHit as e where e.timestamp >= :startTime and e.timestamp <= :endTime
                     group by e.app, e.uri order by count(e.ip) desc
                    """)
    List<ViewStatsDto> getStatsWithoutUris(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

}