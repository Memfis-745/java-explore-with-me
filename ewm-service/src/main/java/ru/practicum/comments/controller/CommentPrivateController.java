package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentShortDto;
import ru.practicum.comments.CommentService;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/comments")
@RequiredArgsConstructor
@Validated
public class CommentPrivateController {

    private final CommentService commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events/{eventId}")
    public CommentShortDto postComment(@PathVariable long userId,
                                       @PathVariable long eventId,
                                       @RequestBody @Valid CommentDto commentDto) {
        log.info("В приватный метод postComment переданы данные: userId = {}, eventId = {}, commentDto = {}", userId, eventId, commentDto);
        return commentService.postComment(userId, eventId, commentDto);
    }

    @PatchMapping("/{commentId}")
    public CommentShortDto updateComment(@PathVariable long userId,
                                        @PathVariable long commentId,
                                        @RequestBody @Valid CommentDto commentDto) {
        log.info("В приватный метод updateComment переданы данные: userId = {}, commentId = {}, commentDto = {}",
                userId, commentId, commentDto);
        return commentService.updateComment(userId, commentId, commentDto);
    }

    @GetMapping("/events/{eventId}")
    public List<CommentShortDto> getCommentsForEvent(@PathVariable long userId,
                                                          @PathVariable long eventId) {
        log.info("В приватный метод getCommentForEvent переданы данные: userId = {}, eventId = {}", userId, eventId);
        return commentService.getCommentsForEvent(userId, eventId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable long userId,
                                        @PathVariable long commentId) {
        log.info("В приватный метод deleteCommentById переданы данные: userId = {}, commentId = {}", userId, commentId);
        commentService.deleteComment(userId, commentId);
    }
}