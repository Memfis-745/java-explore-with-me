package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.CommentService;

@Slf4j
@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
public class CommentAdminController {

    private final CommentService commentService;


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{commentId}")
    public void deleteCommentById(@PathVariable long commentId) {
        log.info("В метод deleteCommentById переданы данные: commentId = {}", commentId);
        commentService.deleteCommentById(commentId);
    }
}