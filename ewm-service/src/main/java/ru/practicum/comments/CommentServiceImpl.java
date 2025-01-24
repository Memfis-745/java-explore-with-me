package ru.practicum.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentFullDto;
import ru.practicum.comments.dto.CommentShortDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.event.EventRepository;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.EntityNotFoundException;
import ru.practicum.user.UserMapper;
import ru.practicum.user.UserRepository;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;

    @Transactional
    @Override
    public CommentShortDto postComment(long userId, long eventId, CommentDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID: " + userId + " не найден"));
        Event event = getEventFromDb(eventId);
        if (event.getPublishedOn() == null) {
            throw new ConflictException("Мероприятие с ID: " + eventId + ", не опуликовано");
        }
        Comment comment = commentRepository.save(CommentMapper.dtoToComment(commentDto, user, event));

        return commentToShortCommentDto(comment, event);
    }

    @Transactional
    @Override
    public CommentShortDto updateComment(long userId, long commentId, CommentDto commentDto) {
        checkUser(userId);
        Comment comment = getCommentFromDb(commentId);
        if (comment.getAuthor().getId() != userId) {
            throw new ConflictException(" Пользователь " + userId + " не является автором комментария " + commentId +
                    ".");
        }
        comment.setText(commentDto.getText());
        Comment updatedComment = commentRepository.save(comment);

        return commentToShortCommentDto(updatedComment, comment.getEvent());
    }

    @Transactional
    @Override
    public void deleteComment(long userId, long commentId) {
        checkUser(userId);
        Comment comment = getCommentFromDb(commentId);
        if (comment.getAuthor().getId() != userId) {
            throw new ConflictException(" Пользователь " + userId + " не является автором комментария " + commentId +
                    ".");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentShortDto> getCommentsForEvent(long userId, long eventId) {
        checkUser(userId);
        Event event = getEventFromDb(eventId);
        EventShortDto eventShortDto = eventService.mapEventsToShortDtos(List.of(event)).get(0);
        return commentRepository.findByAuthorIdAndEventId(userId, eventId).stream()
                .map(comment -> CommentMapper.commentToShortDto(comment, eventShortDto))
                .collect(Collectors.toList());
    }

    @Override
    public CommentFullDto getCommentById(long commentId) {
        Comment comment = getCommentFromDb(commentId);
        UserShortDto userDto = UserMapper.userToShortDto(comment.getAuthor());
        EventShortDto eventShortDto = eventService.mapEventsToShortDtos(List.of(comment.getEvent())).get(0);
        return CommentMapper.commentToFullDto(comment, userDto, eventShortDto);
    }

    @Override
    public List<CommentShortDto> getUserComments(long userId, Integer from, Integer size, String sort) {
        checkUser(userId);

        Pageable page;
        if (SortComments.valueOf(sort.toUpperCase()).equals(SortComments.DESC)) {
            page = PageRequest.of(from / size, size, Sort.by("created").descending());
        } else {
            page = PageRequest.of(from / size, size, Sort.by("created").ascending());
        }

        List<Comment> commentList = commentRepository.findAllByAuthorId(userId, page).toList();
        List<Event> eventsList = commentList.stream()
                .map(Comment::getEvent)
                .collect(Collectors.toList());
        List<EventShortDto> eventShortDtoList = eventService.mapEventsToShortDtos(eventsList);

        Map<Long, EventShortDto> enentsShortMap = new HashMap<>();
        for (EventShortDto eventShortDto : eventShortDtoList) {
            enentsShortMap.put(eventShortDto.getId(), eventShortDto);
        }

        List<CommentShortDto> commentsDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentsDtoList.add(CommentMapper.commentToShortDto(comment, enentsShortMap.get(comment.getEvent().getId())));
        }

        return commentsDtoList;
    }

    @Override
    public List<CommentFullDto> getCommentsByEventId(long eventId, Integer from, Integer size, String sort) {
        Event event = getEventFromDb(eventId);
        EventShortDto eventShortDto = eventService.mapEventsToShortDtos(List.of(event)).get(0);

        Pageable page;
        if (SortComments.valueOf(sort.toUpperCase()).equals(SortComments.DESC)) {
            page = PageRequest.of(from / size, size, Sort.by("created").descending());
        } else {
            page = PageRequest.of(from / size, size, Sort.by("created").ascending());
        }
        List<Comment> commentList = commentRepository.findAllByEventId(eventId, page).toList();

        List<UserShortDto> users = commentList.stream()
                .map(Comment::getAuthor)
                .map(UserMapper::userToShortDto)
                .collect(Collectors.toList());

        Map<Long, UserShortDto> usersMap = new HashMap<>();
        for (UserShortDto user : users) {
            usersMap.put(user.getId(), user);
        }

        List<CommentFullDto> commentFullDtos = new ArrayList<>();
        for (Comment comment : commentList) {
            commentFullDtos.add(CommentMapper.commentToFullDto(comment,
                    usersMap.get(comment.getAuthor().getId()), eventShortDto));
        }
        return commentFullDtos;
    }

    @Transactional
    @Override
    public void deleteCommentById(long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new EntityNotFoundException("Комментарий с ID: " + commentId + " не найден");
        }
        commentRepository.deleteById(commentId);
    }

    private CommentShortDto commentToShortCommentDto(Comment comment, Event event) {
        EventShortDto eventShortDto = eventService.mapEventsToShortDtos(List.of(event)).get(0);
        return CommentMapper.commentToShortDto(comment, eventShortDto);
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

    private Comment getCommentFromDb(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Комментарий с ID: " + commentId + " не найден"));
    }
}