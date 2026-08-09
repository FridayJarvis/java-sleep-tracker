package ru.yandex.practicum.sleeptracker.Function;

import ru.yandex.practicum.sleeptracker.DTO.SleepSession;

import java.util.List;
import java.util.function.Function;

public class TotalSessionsFunction implements Function<List<SleepSession>, String> {
    @Override
    public String apply(List<SleepSession> sleepSessions) {
        return "Сессий сна всего: " + sleepSessions.size();
    }
}
