package ru.yandex.practicum.sleeptracker.Functions;

import ru.yandex.practicum.sleeptracker.DTO.SleepSession;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class MinSessionFunction implements Function<List<SleepSession>, String> {
    @Override
    public String apply(List<SleepSession> sleepSessions) {
        return sleepSessions.stream()
                .min(Comparator.comparing(session -> Duration.between(session.start(), session.finish())))
                .map(session -> "Минимальная " + session)
                .orElse("Минимальная сессия сна: 0 минут");
    }
}