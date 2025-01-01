package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.StatsService;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class StatsController {

    private final StatsService statsService;
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public EndpointHitDto saveStatHit(@RequestBody EndpointHitDto endpointHit) {
        log.info("В метод saveStatHit передан объект с полями: app {}, uri {}, ip {}, timestamp {}",
                endpointHit.getApp(), endpointHit.getUri(), endpointHit.getIp(), endpointHit.getTimestamp());
        return statsService.saveStatsHit(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getVeiwStats(@RequestParam @NotBlank String start,
                                           @RequestParam @NotBlank String end,
                                           @RequestParam(required = false) Set<String> uris,
                                           @RequestParam(defaultValue = "false") boolean unique) {
        log.info("В метод getVeiwStats переданы параметры запроса: start {}, end {}, uri {}, unique {}",
                start, end, uris, unique);
        LocalDateTime startTime = LocalDateTime.parse(start, DATE_FORMAT);
        LocalDateTime endTime = LocalDateTime.parse(end, DATE_FORMAT);

        if (endTime.isBefore(startTime)) {
            throw new BadParameterException("Начало не может быть позже окончания периода");
        }

        return statsService.getViewStats(startTime, endTime, uris, unique);
    }
}