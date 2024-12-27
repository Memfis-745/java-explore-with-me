package ru.practicum;

public class BadParameterException extends RuntimeException {
    public BadParameterException(String message) {
        super(message);
    }
}