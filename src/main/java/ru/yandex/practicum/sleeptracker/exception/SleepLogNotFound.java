package ru.yandex.practicum.sleeptracker.exception;

public class SleepLogNotFound extends RuntimeException {
    public SleepLogNotFound(String message) {
        super(message);
    }
}
