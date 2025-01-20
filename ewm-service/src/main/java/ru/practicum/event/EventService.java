package ru.practicum.event;

import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.request.dto.RequestStatusResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.request.dto.RequestStatusUpdate;


import java.util.List;

public interface EventService {

    List<EventShortDto> getCurrentUserEvents(long userId, Integer from, Integer size);

    EventFullDto postEvent(long userId, NewEventDto eventDto);

    EventFullDto getOwnerEvent(long userId, long eventId);

    EventFullDto patchCurrentUserEvent(long userId, long eventId, UpdateEventUserRequest eventDto);

    List<ParticipationRequestDto> getRequestsOwnersEvent(long userId, long eventId);

    RequestStatusResult patchRequestsOwnersEvent(long userId, long eventId,
                                                 RequestStatusUpdate updateRequest);

    List<EventFullDto> getAdminAllEvent(AdminGetEventParams params);

    EventFullDto patchAdminEvent(long eventId, UpdateEventAdminRequest eventDto);

    List<EventShortDto> getPublicEvents(PublicGetEventParams params, HttpServletRequest request);

    EventFullDto getPublicEventById(long eventId, HttpServletRequest request);

    List<EventShortDto> mapEventsToShortDtos(List<Event> events);

}