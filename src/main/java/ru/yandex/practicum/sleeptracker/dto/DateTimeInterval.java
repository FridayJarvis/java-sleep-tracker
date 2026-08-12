package ru.yandex.practicum.sleeptracker.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record DateTimeInterval(LocalDateTime start, LocalDateTime end) {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
}
