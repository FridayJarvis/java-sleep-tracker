package ru.yandex.practicum.sleeptracker.DTO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public record SleepSession(LocalDateTime start, LocalDateTime finish, SleepQuality sleepQuality) {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    @Override
    public String toString() {
        return "сессия сна:" +
                "\n\t- начало: " + start.format(FORMATTER) +
                "\n\t- конец: " + finish.format(FORMATTER) +
                "\n\t- качество: " + sleepQuality +
                "\n\t- продолжительность: " + Duration.between(start, finish).toMinutes() + " минут";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SleepSession that = (SleepSession) o;
        return Objects.equals(start, that.start) && Objects.equals(finish, that.finish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, finish);
    }
}
