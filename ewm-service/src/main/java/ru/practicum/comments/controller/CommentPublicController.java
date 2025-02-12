package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentFullDto;
import ru.practicum.comments.dto.CommentShortDto;
import ru.practicum.validation.CommentSortConstrain;
import ru.practicum.comments.CommentService;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/comments")
@RequiredArgsConstructor
@Validated
public class CommentPublicController {

    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public CommentFullDto getCommentById(@PathVariable long commentId) {
        log.info("В публичный метод getCommentById переданы данные: commentId = {}", commentId);
        return commentService.getCommentById(commentId);
    }

    @GetMapping("/events/{eventId}")
    public List<CommentFullDto> getCommentsByEventId(@PathVariable long eventId,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                     @RequestParam(defaultValue = "10") @Positive Integer size,
                                                     @RequestParam(defaultValue = "DESC") @CommentSortConstrain String sort) {
        log.info("В публичный метод getCommentsByEventId переданы данные: eventId = {}, from = {}, size = {}, sort = {}",
                eventId, from, size, sort);
        return commentService.getCommentsByEventId(eventId, from, size, sort);
    }


    @GetMapping("/users/{userId}")
    public List<CommentShortDto> getUsersComments(@PathVariable long userId,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(defaultValue = "10") @Positive Integer size,
                                                 @RequestParam(defaultValue = "DESC") @CommentSortConstrain String sort) {
        log.info("В публичный метод getUserComments переданы данные: userId = {}, from = {}, size = {}, sort = {}",
                userId, from, size, sort);
        return commentService.getUserComments(userId, from, size, sort);
    }

}