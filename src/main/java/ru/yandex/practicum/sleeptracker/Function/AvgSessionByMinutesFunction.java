package ru.yandex.practicum.sleeptracker.Function;

import ru.yandex.practicum.sleeptracker.DTO.SleepSession;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;

public class AvgSessionByMinutesFunction implements Function<List<SleepSession>, String> {
    @Override
    public String apply(List<SleepSession> sessions) {
        if (sessions == null) {
            return String.format(Locale.US, "Средняя сессия сна: %.2f минут", 0.0);
        }

        double avgSession = sessions.stream()
                .filter(Objects::nonNull)
                .mapToLong(session -> Duration.between(session.start(), session.end()).toMinutes())
                .average()
                .orElse(0.0);

        return String.format(Locale.US, "Средняя сессия сна: %.2f минут", avgSession);
    }
}
