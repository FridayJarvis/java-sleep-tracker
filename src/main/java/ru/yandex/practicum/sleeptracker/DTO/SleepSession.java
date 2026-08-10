package ru.yandex.practicum.sleeptracker.DTO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public record SleepSession(LocalDateTime start, LocalDateTime end, SleepQuality sleepQuality) {
    @Override
    public String toString() {
        return "сессия сна:" +
                "\n\t- начало: " + start.format(DateTimeInterval.FORMATTER) +
                "\n\t- конец: " + end.format(DateTimeInterval.FORMATTER) +
                "\n\t- качество: " + sleepQuality +
                "\n\t- продолжительность: " + Duration.between(start, end).toMinutes() + " минут";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SleepSession that = (SleepSession) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
