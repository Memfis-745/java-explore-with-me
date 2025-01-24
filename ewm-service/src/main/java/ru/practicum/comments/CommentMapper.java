package ru.practicum.comments;

import lombok.experimental.UtilityClass;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentFullDto;
import ru.practicum.comments.dto.CommentShortDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public CommentShortDto commentToShortDto(Comment comment, EventShortDto eventDto) {
        return CommentShortDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .event(eventDto)
                .created(comment.getCreated())
                .build();
    }

    public Comment dtoToComment(CommentDto commentDto, User user, Event event) {
        return Comment.builder()
                .text(commentDto.getText())
                .author(user)
                .event(event)
                .created(LocalDateTime.now())
                .build();
    }

    public CommentFullDto commentToFullDto(Comment comment, UserShortDto userDto, EventShortDto eventDto) {
        return CommentFullDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(userDto)
                .event(eventDto)
                .created(comment.getCreated())
                .build();
    }
}