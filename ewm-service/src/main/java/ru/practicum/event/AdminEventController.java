package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.AdminGetEventParams;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;

import jakarta.validation.Valid;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class AdminEventController {

    private final EventService eventService;
    private String query;


    @GetMapping("")
    public List<EventFullDto> getAllEvent(@Valid AdminGetEventParams params) {
        log.info("Админ-Запрос на получение отфильтрованных событий: params = {}", params);
        List<EventFullDto> rturn = eventService.getAdminAllEvent(params);
        log.info("Лист на выходе отфильтрованных событий: return = {}", rturn);
        return rturn;
        //return eventService.getAdminAllEvent(params);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEvent(@PathVariable long eventId,
                                   @RequestBody @Valid UpdateEventAdminRequest eventDto) {
        log.info("Админ-запрос на редактирование события - patchEvent. переданы данные: eventId = {}, eventDto = {}",
                eventId, eventDto);

        return eventService.patchAdminEvent(eventId, eventDto);
    }
}