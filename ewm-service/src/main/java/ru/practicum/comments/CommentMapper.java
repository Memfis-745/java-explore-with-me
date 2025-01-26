package ru.practicum.comments;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentFullDto;
import ru.practicum.comments.dto.CommentShortDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "comment.id", target = "id")
    @Mapping(target = "event", source = "eventDto")
    CommentShortDto commentToShortDto(Comment comment, EventShortDto eventDto);

    @Mapping(source = "comment.id", target = "id")
    @Mapping(target = "author", source = "userDto")
    @Mapping(target = "event", source = "eventDto")
    CommentFullDto commentToFullDto(Comment comment, UserShortDto userDto, EventShortDto eventDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(source = "user", target = "author")
    @Mapping(source = "event", target = "event")
    Comment dtoToComment(CommentDto commentDto, User user, Event event);


}