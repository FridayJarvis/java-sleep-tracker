package ru.yandex.practicum.sleeptracker.Exception;

public class SessionParseException extends RuntimeException {
    public SessionParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
