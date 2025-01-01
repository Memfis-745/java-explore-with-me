package ru.practicum.dto;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import ru.practicum.EndpointHitDto;
import ru.practicum.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@UtilityClass
public class EndpointHitMapper {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EndpointHitDto toDto(EndpointHit hit) {
        return new EndpointHitDto(
                hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp() != null ? hit.getTimestamp().format(DATE_FORMAT) : null
        );
    }

    public EndpointHit toModel(EndpointHitDto hitDto) {
        return new EndpointHit(
                0L,
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                StringUtils.isNotBlank(hitDto.getTimestamp()) ? LocalDateTime.parse(hitDto.getTimestamp(), DATE_FORMAT) : null
        );
    }
}