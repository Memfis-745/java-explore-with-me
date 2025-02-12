package ru.practicum.comments;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentFullDto;
import ru.practicum.comments.dto.CommentShortDto;

import java.util.List;

public interface CommentService {
    CommentShortDto postComment(long userId, long eventId, CommentDto commentDto);

    CommentShortDto updateComment(long userId, long commentId, CommentDto commentDto);

    List<CommentShortDto> getCommentsForEvent(long userId, long eventId);

    CommentFullDto getCommentById(long commentId);

    List<CommentShortDto> getUserComments(long userId, Integer from, Integer size, String sort);

    List<CommentFullDto> getCommentsByEventId(long eventId, Integer from, Integer size, String sort);

    void deleteComment(long userId, long commentId);

    void deleteCommentById(long commentId);
}