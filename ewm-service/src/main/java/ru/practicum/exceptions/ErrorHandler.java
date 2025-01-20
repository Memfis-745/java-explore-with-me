package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppiError handleBindExc(BindException e) {

        String errors = getErrors(e);
        String status = HttpStatus.BAD_REQUEST.name();
        String reason = "В параметр запроса передан неправльный агрумент";
        log.info("Validation message: {}, status: {}, response: {}", e.getMessage(), status, reason);
        return new AppiError(errors, e.getMessage(), reason, status, LocalDateTime.now().format(Constants.DATE_FORMAT));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppiError handleConstraintViolationExc(RuntimeException e) {

        String errors = getErrors(e);
        String status = HttpStatus.BAD_REQUEST.name();
        String reason = "В параметр запроса передан неправльный агрумент";
        log.info("Validation message: {}, status: {}, response: {}", e.getMessage(), status, reason);
        return new AppiError(errors, e.getMessage(), reason, status, LocalDateTime.now().format(Constants.DATE_FORMAT));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppiError handleMissingArgumentExc(MissingServletRequestParameterException e) { // Exception

        String errors = getErrors(e);
        String status = HttpStatus.BAD_REQUEST.name();
        String reason = "Отсутствует аргумент параметра пути";
        log.info("Validation message: {}, status: {}, response: {}", e.getMessage(), status, reason);
        return new AppiError(errors, e.getMessage(), reason, status, LocalDateTime.now().format(Constants.DATE_FORMAT));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AppiError handleOtherExc(Throwable e) {
        log.info("Error: ", e);
        String errors = getErrors(e);
        String reason = "Unexpected error";
        return new AppiError(errors, e.getMessage(), reason, HttpStatus.INTERNAL_SERVER_ERROR.name(), LocalDateTime.now().format(Constants.DATE_FORMAT));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AppiError handleNotFoundExc(EntityNotFoundException e) {

        String errors = getErrors(e);
        String reason = "The required object was not found.";
        log.info("Element not found: {}", e.getMessage());
        return new AppiError(errors, e.getMessage(), reason, HttpStatus.NOT_FOUND.name(), LocalDateTime.now().format(Constants.DATE_FORMAT));
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public AppiError handleConflictExistExc(RuntimeException e) {

        String errors = getErrors(e);
        String status = HttpStatus.CONFLICT.name();
        String reason = "Конфликт запроса и базы данных";
        log.info("Validation message: {}, status: {}, response: {}", e.getMessage(), status, reason);
        return new AppiError(errors, e.getMessage(), reason, status, LocalDateTime.now().format(Constants.DATE_FORMAT));
    }

    private String getErrors(Throwable e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
}