package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.DTO.SleepSession;
import ru.yandex.practicum.sleeptracker.DTO.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.Functions.TotalSleepSessionsFunction;
import ru.yandex.practicum.sleeptracker.IO.SleepLogs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {
    private static List<Function<List<SleepSession>, SleepAnalysisResult>> functions = new ArrayList<>();

    public static void main(String[] args) {
        List<SleepSession> sleepSessions = SleepLogs.load("SleepLogs.txt");
        functions.add(new TotalSleepSessionsFunction());
        SleepAnalysisResult result = functions.get(0).apply(sleepSessions);
        System.out.println(result.description() + " " + result.value());
    }
}