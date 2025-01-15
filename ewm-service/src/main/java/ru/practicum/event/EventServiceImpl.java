package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.request.dto.RequestStatusResult;
import ru.practicum.request.dto.RequestStatusUpdate;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.ViewStatsDto;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.EntityNotFoundException;
import ru.practicum.location.LocationMapper;
import ru.practicum.location.LocationRepository;
import ru.practicum.location.model.Location;
import ru.practicum.request.RequestMapper;
import ru.practicum.request.RequestRepository;
import ru.practicum.request.Status;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.event.dto.*;

import java.time.LocalDateTime;
import java.util.*;

import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static ru.practicum.exceptions.Constants.APP_NAME;
import static ru.practicum.exceptions.Constants.DATE_FORMAT;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;


    @Override
    public List<EventShortDto> getCurrentUserEvents(long userId, Integer from, Integer size) {
        checkUser(userId);
        Pageable page = PageRequest.of(from / size, size);

        List<Event> events = eventRepository.findAllByInitiatorId(userId, page).toList();

        return mapEventsToShortDtos(events);
    }

    @Transactional
    @Override
    public EventFullDto postEvent(long userId, NewEventDto eventDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID: " + userId + " не найден"));
        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new EntityNotFoundException("Категория с ID: " + eventDto.getCategory() + " не найден"));

        Location location = locationRepository.save(LocationMapper.toModel(eventDto.getLocation()));

        Event event = eventRepository.save(EventMapper.toEvent(eventDto, user, category, location));
        return EventMapper.toFullDto(event, 0, 0L);
    }

    @Override
    public EventFullDto getOwnerEvent(long userId, long eventId) {

        Event event = checkUserAndEventId(userId, eventId);
        checkEventInitiator(event, userId);

        return mapEventsToFullDtos(List.of(event)).get(0);
    }

    @Transactional
    @Override
    public EventFullDto patchCurrentUserEvent(long userId, long eventId, UpdateEventUserRequest eventDto) {

        Event event = checkUserAndEventId(userId, eventId);
        checkEventInitiator(event, userId);

        Category category = null;
        if (eventDto.getCategory() != null) {
            category = categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new EntityNotFoundException("Категория с ID: " + eventDto.getCategory() + " не найдена"));
        }

        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Изменять можно только отмененные или еще не опубликованные события");
        }
        Location location = null;
        if (eventDto.getLocation() != null) {
            location = locationRepository.save(LocationMapper.toModel(eventDto.getLocation()));
        }

        Event eventFromDb = eventRepository.save(EventMapper.userUpdateDtoToEvent(event, eventDto, category, location));
        return mapEventsToFullDtos(List.of(eventFromDb)).get(0);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsOwnersEvent(long userId, long eventId) {

        Event event = checkUserAndEventId(userId, eventId);
        checkEventInitiator(event, userId);

        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::requestToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public RequestStatusResult patchRequestsOwnersEvent(long userId, long eventId,
                                                        RequestStatusUpdate updateRequest) {
        Event event = checkUserAndEventId(userId, eventId);
        checkEventInitiator(event, userId);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return mapRequestsToResult(eventId);
        }

        return updateRequestStatusToResult(event, updateRequest);
    }

    @Override
    public List<EventFullDto> getAdminAllEvent(AdminGetEventParams params) {

        LocalDateTime start = params.getStartDateTime();
        LocalDateTime end = params.getEndDateTime();
        List<Long> categories = params.getCategories();
        List<Long> users = params.getUsers();
        List<String> states = params.getStates();

        Pageable page = PageRequest.of(params.getFrom() / params.getSize(), params.getSize());
        //  List<Event> events = new ArrayList();
        // if (users.equals(null) && categories.equals(null) && states.equals(null)) {
        //    events = eventRepository.findAll();
        //  } else {
        List<Event> events = eventRepository.getAllEventParams(users, states, categories, start, end);
        List<Event> eventAll = eventRepository.getAllEventParamsq();
        // }

        log.info("Значение from и ensized  на входе в репозиторий  = {}, {}", params.getFrom(), params.getSize());
        //Page<Event> events = eventRepository.getAllEventParams(users, states, categories, start, end, page);

        log.info("Значение PageEvents//Лист в методе сервис импл  = {}", events);
        log.info("Значение PageEvents//Лист в методе сервис импл  = {}", eventAll);

        //List<EventFullDto> eventFullDtoList =  mapEventsToFullDtos(events.toList());
        List<EventFullDto> eventFullDtoList = mapEventsToFullDtos(events);
        eventFullDtoList.stream()
                .skip(params.getFrom())
                .limit(params.getSize())
                .toList();
        log.info("Значение eventFullDtoList в методе сервис импл  = {}", eventFullDtoList);
        return eventFullDtoList;

    }

    @Transactional
    @Override
    public EventFullDto patchAdminEvent(long eventId, UpdateEventAdminRequest eventDto) {

        Event event = getEventFromDb(eventId);
        Category category = null;
        if (eventDto.getCategory() != null) {
            category = categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new EntityNotFoundException("Категория с ID: " + eventDto.getCategory() + " не найдена"));
        }

        if (event.getPublishedOn() != null && event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ConflictException("Изменять событие можно только не позднее чем за час до его начала");
        }

        if (eventDto.getStateAction() != null) {
            if (AdminStateAction.valueOf(eventDto.getStateAction()).equals(AdminStateAction.PUBLISH_EVENT)) {
                if (!event.getState().equals(State.PENDING)) {
                    throw new ConflictException("Опубликовать можно только событие имеющее статус PENDING");
                }
            } else {
                if (!event.getState().equals(State.PENDING)) {
                    throw new ConflictException("Событие можно отклонить, только если оно еще не опубликовано");
                }
            }
        }

        Location location = null;
        if (eventDto.getLocation() != null) {
            location = locationRepository.save(LocationMapper.toModel(eventDto.getLocation()));
        }

        Event eventFromDb = eventRepository.save(EventMapper.adminUpdateDtoToEvent(event, eventDto, category, location));

        return mapEventsToFullDtos(List.of(eventFromDb)).get(0);
    }

    @Override
    public List<EventShortDto> getPublicEvents(PublicGetEventParams params, HttpServletRequest request) {

        LocalDateTime start = params.getStartDateTime();
        LocalDateTime end = params.getEndDateTime();
        String text = params.getText();
        Boolean paid = params.getPaid();
        State state = State.PUBLISHED;

        log.info("Значение start и end  на входе в репозиторий  = {}, {}", start, end);


        if ((start != null) && (start.isAfter(end))) {
            throw new ConflictException("Дата окончания события не может быть раньше даты начала");
        }
        text = isNull(text) ? null : text.toLowerCase();
        Pageable page = PageRequest.of(params.getFrom() / params.getSize(), params.getSize());

        Page<Event> events = eventRepository.getPublicEventsWithFilter(state, text, paid, start, end, page);

        List<EventShortDto> shortDtoList = mapEventsToShortDtos(events.toList());
        if (Sort.valueOf(params.getSort().toUpperCase()).equals(Sort.EVENT_DATE)) {
            shortDtoList = shortDtoList.stream()
                    .sorted(Comparator.comparing(EventShortDto::getEventDate))
                    .collect(Collectors.toList());
        } else {
            shortDtoList = shortDtoList.stream()
                    .sorted(Comparator.comparingLong(EventShortDto::getViews))
                    .collect(Collectors.toList());
        }

        EndpointHitDto hitDto = new EndpointHitDto(null, APP_NAME, request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now().format(DATE_FORMAT));
        statsClient.postHit(hitDto);

        return shortDtoList;
    }


    @Override
    public EventFullDto getPublicEventById(long eventId, HttpServletRequest request) {

        Event event = getEventFromDb(eventId);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new EntityNotFoundException("События с ID: " + eventId + " не найдено среди опубликованных");
        }

        EventFullDto eventFullDto = mapEventsToFullDtos(List.of(event)).get(0);

        EndpointHitDto hitDto = new EndpointHitDto(null, APP_NAME, request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now().format(DATE_FORMAT));
        statsClient.postHit(hitDto);

        return eventFullDto;
    }

    @Override
    public List<EventShortDto> mapEventsToShortDtos(List<Event> events) {

        List<Long> eventsIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        Optional<LocalDateTime> start = eventRepository.getMinPublishedDate(eventsIds);

        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        if (start.isPresent()) {
            Map<Long, Long> veiws = getStatsForEvents(start.get(), eventsIds);
            Map<Long, Integer> eventsRequests = getEventRequests(Status.CONFIRMED, eventsIds);
            for (Event event : events) {
                eventShortDtoList.add(
                        EventMapper.toShortDto(event,
                                eventsRequests.getOrDefault(event.getId(), 0),
                                veiws.getOrDefault(event.getId(), 0L),
                                null)
                );
            }
        } else {
            for (Event event : events) {
                eventShortDtoList.add(EventMapper.toShortDto(event, 0, 0L, 0L));
            }
        }

        return eventShortDtoList;
    }

    private List<EventFullDto> mapEventsToFullDtos(List<Event> events) {

        List<Long> eventsIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        Optional<LocalDateTime> start = eventRepository.getMinPublishedDate(eventsIds);

        List<EventFullDto> eventShortDtoList = new ArrayList<>();
        if (start.isPresent()) {
            Map<Long, Long> views = getStatsForEvents(start.get(), eventsIds);
            Map<Long, Integer> eventsRequests = getEventRequests(Status.CONFIRMED, eventsIds);
            for (Event event : events) {
                eventShortDtoList.add(EventMapper.toFullDto(event,
                        eventsRequests.getOrDefault(event.getId(), 0),
                        views.getOrDefault(event.getId(), 0L))
                );
            }
        } else {
            for (Event event : events) {
                eventShortDtoList.add(EventMapper.toFullDto(event, 0, 0L));
            }
        }

        return eventShortDtoList;
    }

    private Map<Long, Long> getStatsForEvents(LocalDateTime start, List<Long> eventsIds) {

        List<String> uries = eventsIds.stream()
                .map(id -> "/events/" + id)
                .collect(Collectors.toList());

        List<ViewStatsDto> veiwStatsDtoList = statsClient.getStats(start, LocalDateTime.now(), uries, true);

        Map<Long, Long> veiws = new HashMap<>();

        for (ViewStatsDto veiwStatsDto : veiwStatsDtoList) {
            String uri = veiwStatsDto.getUri();
            Long eventId = Long.parseLong(uri.substring(uri.lastIndexOf("/") + 1));
            veiws.put(eventId, veiwStatsDto.getHits());
        }
        return veiws;
    }

    private Map<Long, Integer> getEventRequests(Status status, List<Long> eventsIds) {
        List<Request> requestList = requestRepository.findAllByStatusAndEventIdIn(status, eventsIds);
        Map<Long, Integer> eventsRequests = new HashMap<>();
        for (Request request : requestList) {
            if (eventsRequests.containsKey(request.getId())) {
                Integer count = eventsRequests.get(request.getId());
                eventsRequests.put(request.getEvent().getId(), ++count);
            } else {
                eventsRequests.put(request.getEvent().getId(), 1);
            }
        }
        return eventsRequests;
    }

    private RequestStatusResult mapRequestsToResult(long eventId) {
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        List<Request> requestsList = requestRepository.findAllByEventId(eventId);
        for (Request request : requestsList) {
            if (request.getStatus().equals(Status.REJECTED)) {
                rejectedRequests.add(RequestMapper.requestToDto(request));
            } else if (request.getStatus().equals(Status.CONFIRMED)) {
                confirmedRequests.add(RequestMapper.requestToDto(request));
            }
        }
        return new RequestStatusResult(confirmedRequests, rejectedRequests);
    }

    private RequestStatusResult updateRequestStatusToResult(Event event,
                                                            RequestStatusUpdate updateRequest) {
        Integer confirmRequests = requestRepository.countAllByStatusAndEventId(Status.CONFIRMED, event.getId());
        if (event.getParticipantLimit() <= confirmRequests) {
            throw new ConflictException("На событие с ID: " + event.getId() +
                    " уже зарегистрировано максимально кол-во участников: " + confirmRequests);
        }

        List<Request> requestList = requestRepository.findAllByIdIn(updateRequest.getRequestIds());

        int counter = 0;
        int requestsToUpdate = event.getParticipantLimit() - confirmRequests;
        List<Long> requestsIdsForConfirm = new ArrayList<>(requestsToUpdate);
        List<Long> requestsIdsForReject = new ArrayList<>(confirmRequests);

        List<Request> confirmedRequestList = requestRepository.findAllByEventIdAndStatus(event.getId(), Status.CONFIRMED);
        List<Request> rejectRequestList = requestRepository.findAllByEventIdAndStatus(event.getId(), Status.REJECTED);

        for (Request request : requestList) {
            if (!request.getStatus().equals(Status.PENDING)) {
                throw new ConflictException("У запроса с ID: " + request.getId() + "статус: " + request.getStatus() +
                        ", ожидалось PENDING");
            }
            if (!Objects.equals(request.getEvent().getId(), event.getId())) {
                throw new ConflictException("Запрос с ID: " + request.getId() + "относится к событию с ID: "
                        + request.getEvent().getId() + ", а не к событию с " + event.getId());
            }
            if (updateRequest.getStatus().equals(Status.CONFIRMED.name()) && counter < requestsToUpdate) {
                requestsIdsForConfirm.add(request.getId());
                request.setStatus(Status.CONFIRMED);
                confirmedRequestList.add(request);
                counter++;
            } else {
                requestsIdsForReject.add(request.getId());
                request.setStatus(Status.REJECTED);
                rejectRequestList.add(request);
            }
        }
        requestRepository.requestStatusUpdate(Status.valueOf(updateRequest.getStatus()), requestsIdsForConfirm);
        if (!requestsIdsForReject.isEmpty()) {
            requestRepository.requestStatusUpdate(Status.REJECTED, requestsIdsForConfirm);
        }

        List<ParticipationRequestDto> confirmedRequests = confirmedRequestList.stream()
                .map(RequestMapper::requestToDto)
                .collect(Collectors.toList());

        List<ParticipationRequestDto> rejectedRequests = rejectRequestList.stream()
                .map(RequestMapper::requestToDto)
                .collect(Collectors.toList());

        return new RequestStatusResult(confirmedRequests, rejectedRequests);
    }

    private Event checkUserAndEventId(long userId, long eventId) {
        checkUser(userId);
        return getEventFromDb(eventId);
    }

    private void checkEventInitiator(Event event, long userId) {
        if (event.getInitiator().getId() != userId) {
            throw new ConflictException("Событие с ID: " + event.getId() + ", создано пользователем с ID: " +
                    event.getInitiator().getId() + ", а не пользователем с ID: " + userId);
        }
    }

    private void checkUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Пользователь с ID: " + userId + " не найден");
        }
    }

    private Event getEventFromDb(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("События с ID: " + eventId + " не найдено"));
    }

}