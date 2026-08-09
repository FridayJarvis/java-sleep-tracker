package ru.yandex.practicum.sleeptracker.Function;

import ru.yandex.practicum.sleeptracker.DTO.SleepQuality;
import ru.yandex.practicum.sleeptracker.DTO.SleepSession;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class BadQualitySessionsFunction implements Function<List<SleepSession>, String> {
    @Override
    public String apply(List<SleepSession> sleepSessions) {
        long badSessions = sleepSessions.stream()
                .filter(Objects::nonNull)
                .filter(session -> session.sleepQuality() == SleepQuality.BAD)
                .count();

        return "Количество сессий с плохим качеством сна: " + badSessions;
    }
}
