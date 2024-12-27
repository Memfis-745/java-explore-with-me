package ru.practicum.service;

import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface StatsService {
    EndpointHitDto saveStatsHit(EndpointHitDto endpointHit);

    List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, Set<String> uris, boolean unique);
}