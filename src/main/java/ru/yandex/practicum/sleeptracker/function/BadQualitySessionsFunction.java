package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.dto.SleepQuality;
import ru.yandex.practicum.sleeptracker.dto.SleepSession;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class BadQualitySessionsFunction implements Function<List<SleepSession>, String> {
    @Override
    public String apply(List<SleepSession> sessions) {
        if (sessions == null) {
            return "Количество сессий с плохим качеством сна: 0";
        }
        long badSessions = sessions.stream()
                .filter(Objects::nonNull)
                .filter(session -> session.sleepQuality() == SleepQuality.BAD)
                .count();

        return "Количество сессий с плохим качеством сна: " + badSessions;
    }
}
