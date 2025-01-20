package ru.practicum.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppiError {

    private final String error;

    private final String message;

    private final String reason;

    private final String status;

    private final String timestamp;
}