package ru.yandex.practicum.sleeptracker.DTO;

import ru.yandex.practicum.sleeptracker.SleepQuality;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record SleepSession(LocalDateTime start, LocalDateTime finish, SleepQuality sleepQuality) {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
}
