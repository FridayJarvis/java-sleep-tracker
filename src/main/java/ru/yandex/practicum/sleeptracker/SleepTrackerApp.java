package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.DTO.SleepSession;
import ru.yandex.practicum.sleeptracker.Exception.SleepLogNotFound;
import ru.yandex.practicum.sleeptracker.Function.*;
import ru.yandex.practicum.sleeptracker.IO.SleepLogs;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

public class SleepTrackerApp {
    private static final List<Function<List<SleepSession>, String>> functions = new ArrayList<>();
    private static List<SleepSession> sleepSessions;

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new SleepLogNotFound("Нет аргументов командой строки при запуске программы.\n" +
                    "Нужно передать аргумент командной строки в качестве пути лога сна при запуске.");
        }

        Path sleepLogPath = Path.of(args[0]);
        if (!Files.isRegularFile(sleepLogPath)) {
            throw new SleepLogNotFound("Неправильный path-аргумент командной строки при запуске программы: " + sleepLogPath +
                    "\nФайла по данному пути не существует. Передайте актуальный путь аргументом при запуске программы.");
        }

        sleepSessions = SleepLogs.load(sleepLogPath);

        functions.add(new TotalSessionsFunction());
        functions.add(new MinSessionByMinutesFunction());
        functions.add(new MaxSessionByMinutesFunction());
        functions.add(new AvgSessionByMinutesFunction());
        functions.add(new BadQualitySessionsFunction());
        functions.add(new SleeplessNightsFunction());

        functions.stream()
                .map(function -> function.apply(sleepSessions))
                .forEach(System.out::println);
    }

    public static Optional<SleepSession> theEarliestSession() {
        return sleepSessions.stream()
                .min(Comparator.comparing(SleepSession::start));
    }

    public static Optional<SleepSession> theLastSession() {
        return sleepSessions.stream()
                .max(Comparator.comparing(SleepSession::start));
    }
}