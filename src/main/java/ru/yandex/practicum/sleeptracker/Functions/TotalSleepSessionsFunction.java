package ru.yandex.practicum.sleeptracker.Functions;

import ru.yandex.practicum.sleeptracker.DTO.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.DTO.SleepSession;

import java.util.List;
import java.util.function.Function;

public class TotalSleepSessionsFunction implements Function<List<SleepSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sleepSessions) {
        return new SleepAnalysisResult("Сессий сна всего:", String.valueOf(sleepSessions.size()));
    }
}
