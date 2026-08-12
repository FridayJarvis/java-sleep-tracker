package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.dto.SleepSession;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class MinSessionByMinutesFunction implements Function<List<SleepSession>, String> {
    @Override
    public String apply(List<SleepSession> sessions) {
        if (sessions == null) {
            return "Минимальная сессия сна: 0 минут";
        }

        return sessions.stream()
                .min(Comparator.comparing(session -> Duration.between(session.start(), session.end())))
                .map(session -> "Минимальная " + session)
                .orElse("Минимальная сессия сна: 0 минут");
    }
}