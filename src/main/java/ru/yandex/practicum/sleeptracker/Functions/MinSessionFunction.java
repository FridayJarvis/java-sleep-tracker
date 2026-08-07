package ru.yandex.practicum.sleeptracker.Functions;

import ru.yandex.practicum.sleeptracker.DTO.SleepSession;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MinSessionFunction implements Function<List<SleepSession>, String> {
    private final static String RESULT = "Минимальная сессия сна: %s минут";
    @Override
    public String apply(List<SleepSession> sleepSessions) {
        return sleepSessions.stream()
                .map(session -> Duration.between(session.start(), session.finish()).toMinutes())
                .min(Long::compareTo)
                .map(minMinutes -> String.format(RESULT, minMinutes))
                .orElseGet(() -> String.format(RESULT, 0));
    }
}