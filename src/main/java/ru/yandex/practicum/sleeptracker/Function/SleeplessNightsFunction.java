package ru.yandex.practicum.sleeptracker.Function;

import ru.yandex.practicum.sleeptracker.DTO.DateTimeInterval;
import ru.yandex.practicum.sleeptracker.DTO.SleepSession;
import ru.yandex.practicum.sleeptracker.SleepTrackerApp;

import java.time.*;
import java.util.List;
import java.util.function.Function;

public class SleeplessNightsFunction implements Function<List<SleepSession>, String> {

    @Override
    public String apply(List<SleepSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return "Бессонных ночей: 0";
        }

        LocalDateTime firstDateTime = SleepTrackerApp.theEarliestSession().orElseThrow().start();
        if (firstDateTime.getHour() >= 12) {
            firstDateTime = firstDateTime.plusDays(1);
        }

        LocalDateTime lastDateTime = SleepTrackerApp.theLastSession().orElseThrow().end();

        long sleeplessNights = firstDateTime.toLocalDate().datesUntil(lastDateTime.toLocalDate().plusDays(1))
                .map(date -> {
                    LocalDateTime nightStart = LocalDateTime.of(date, LocalTime.MIDNIGHT);
                    LocalDateTime nightFinish = LocalDateTime.of(date, LocalTime.of(6, 0));
                    return new DateTimeInterval(nightStart, nightFinish);
                }).filter(night ->
                        sessions.stream().noneMatch(session ->
                                session.start().isBefore(night.end()) && session.end().isAfter(night.start()) &&
                                session.start().isBefore(session.end())))
                .count();
        return "Бессонных ночей: " + sleeplessNights;
    }
}
