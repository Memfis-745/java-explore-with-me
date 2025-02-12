package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.request.dto.RequestStatusUpdate;
import ru.practicum.request.dto.RequestStatusResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class PrivateEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getUsersEvents(@PathVariable long userId,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("В приватный метод getUsersEvents переданы данные: userId = {}, from = {}, size = {}", userId, from, size);
        return eventService.getCurrentUserEvents(userId, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EventFullDto createEvent(@PathVariable long userId,
                                    @RequestBody @Valid NewEventDto eventDto) {
        log.info("В приватный метод postEvent переданы данные " +
                "для создании события: userId = {}, eventDto = {}", userId, eventDto);
        return eventService.postEvent(userId, eventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getOwnerEvent(@PathVariable long userId,
                                      @PathVariable long eventId) {
        log.info("В приватный метод getCurrentUserEvents переданы данные для получения информации" +
                " пользователя: userId = {}, eventId = {}", userId, eventId);
        return eventService.getOwnerEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchCurrentUserEvent(@PathVariable long userId,
                                              @PathVariable long eventId,
                                              @RequestBody @Valid UpdateEventUserRequest eventDto) {
        log.info("В приватный метод patchCurrentUserEvent переданы данные для " +
                        "редактирования события пользователем: userId = {}, eventId = {}, eventDto = {}",
                userId, eventId, eventDto);
        return eventService.patchCurrentUserEvent(userId, eventId, eventDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForOwnersEvent(@PathVariable long userId,
                                                                   @PathVariable long eventId) {
        log.info("В приватный метод getRequestsForOwnersEvent передан запрос о событии пользователя: userId = {}, eventId = {}", userId, eventId);
        return eventService.getRequestsOwnersEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public RequestStatusResult updateRequestsForOwnersEvent(@PathVariable long userId,
                                                            @PathVariable long eventId,
                                                            @RequestBody RequestStatusUpdate updateRequest) {
        log.info("В приватный метод patchRequestsForOwnersEvent передан запрос " +
                        "на обновление статуса заявки: userId = {}, eventId = {}, updateRequest = {}",
                userId, eventId, updateRequest);
        return eventService.patchRequestsOwnersEvent(userId, eventId, updateRequest);
    }
}