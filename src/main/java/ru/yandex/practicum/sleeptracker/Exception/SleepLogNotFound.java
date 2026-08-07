package ru.yandex.practicum.sleeptracker.Exception;

public class SleepLogNotFound extends RuntimeException {
    public SleepLogNotFound(String message) {
        super(message);
    }
}
