package ru.yandex.practicum.sleeptracker.Function;

import ru.yandex.practicum.sleeptracker.DTO.SleepSession;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class TotalSessionsFunction implements Function<List<SleepSession>, String> {
    @Override
    public String apply(List<SleepSession> sessions) {
        if (sessions == null) {
            return "Сессий сна всего: 0";
        }
        return "Сессий сна всего: " + sessions.stream().filter(Objects::nonNull).count();
    }
}
