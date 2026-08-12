package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.dto.SleepSession;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class MaxSessionByMinutesFunction implements Function<List<SleepSession>, String> {
    @Override
    public String apply(List<SleepSession> sessions) {
        if (sessions == null) {
            return "Максимальная сессия сна: 0 минут";
        }

        return sessions.stream()
                .max(Comparator.comparing(session -> Duration.between(session.start(), session.end())))
                .map(session -> "Максимальная " + session)
                .orElse("Максимальная сессия сна: 0 минут");
    }
}
