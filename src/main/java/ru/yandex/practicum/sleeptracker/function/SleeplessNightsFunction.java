package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.dto.DateTimeInterval;
import ru.yandex.practicum.sleeptracker.dto.SleepSession;
import ru.yandex.practicum.sleeptracker.SleepTrackerApp;

import java.time.*;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class SleeplessNightsFunction implements Function<List<SleepSession>, String> {

    @Override
    public String apply(List<SleepSession> sessions) {
        if (sessions == null) {
            return "Бессонных ночей: 0";
        }

        List<SleepSession> validSessions = sessions.stream()
                .filter(Objects::nonNull)
                .filter(session -> session.start().isBefore(session.end()))
                .toList();

        if (validSessions.isEmpty()) {
            return "Бессонных ночей: 0";
        }

        LocalDateTime firstStartDateTime = SleepTrackerApp.theEarliestSession(validSessions).orElseThrow().start();
        if (firstStartDateTime.getHour() >= 12) {
            firstStartDateTime = firstStartDateTime.plusDays(1);
        }

        LocalDateTime lastStartDateTime = SleepTrackerApp.theLastSession(validSessions).orElseThrow().start();
        if (lastStartDateTime.getHour() >= 12) {
            lastStartDateTime = lastStartDateTime.plusDays(1);
        }

        long sleeplessNights = firstStartDateTime.toLocalDate().datesUntil(lastStartDateTime.toLocalDate().plusDays(1))
                .map(date -> {
                    LocalDateTime nightStart = LocalDateTime.of(date, LocalTime.MIDNIGHT);
                    LocalDateTime nightFinish = LocalDateTime.of(date, LocalTime.of(6, 0));
                    return new DateTimeInterval(nightStart, nightFinish);
                }).filter(night ->
                        validSessions.stream()
                                .noneMatch(session ->
                                        session.start().isBefore(night.end()) && session.end().isAfter(night.start())))
                .count();
        return "Бессонных ночей: " + sleeplessNights;
    }
}
