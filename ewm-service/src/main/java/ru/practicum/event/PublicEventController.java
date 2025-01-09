package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.PublicGetEventParams;
import ru.practicum.validation.StartBeforeEndConstrain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Validated
public class PublicEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getPublicEvents(@Valid @StartBeforeEndConstrain PublicGetEventParams params,
                                               HttpServletRequest request) {
        log.info("В публичный метод getPublicEvents передан запрос для получения событий: params = {}", params);
        return eventService.getPublicEvents(params, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getPublicEventById(@PathVariable long id,
                                           HttpServletRequest request) {
        log.info("В метод getPublicEventById передан запрос на получение события по id: id = {}", id);
        return eventService.getPublicEventById(id, request);
    }
}