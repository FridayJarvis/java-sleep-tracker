package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.DTO.SleepSession;
import ru.yandex.practicum.sleeptracker.Functions.MinSessionFunction;
import ru.yandex.practicum.sleeptracker.Functions.TotalSleepSessionsFunction;
import ru.yandex.practicum.sleeptracker.IO.SleepLogs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {
    private static final List<Function<List<SleepSession>, String>> functions = new ArrayList<>();

    public static void main(String[] args) {
        List<SleepSession> sleepSessions = SleepLogs.load("SleepLogs.txt");

        functions.add(new TotalSleepSessionsFunction());
        functions.add(new MinSessionFunction());

        for (var function : functions) {
            System.out.println(function.apply(sleepSessions));
        }
    }
}