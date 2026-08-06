package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.Exception.SessionParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class SleepLogs {
    private SleepLogs() {
    }

    public static List<SleepSession> load(final String sleepLogPath) {
        try (final BufferedReader reader = Files.newBufferedReader(
                Path.of(sleepLogPath), StandardCharsets.UTF_8)) {
            return reader.lines()
                    .map(SleepLogs::parseSleepLogLine)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("The stream of output broke down");
        }
    }

    private static Optional<SleepSession> parseSleepLogLine(final String sleepLogLine) {
        if (sleepLogLine == null || sleepLogLine.isBlank()) {
            return Optional.empty();
        }

        String[] splitLogLine = sleepLogLine.split(";");

        final int VALID_LENGTH_SPLIT_LINE = 3;
        if (splitLogLine.length != VALID_LENGTH_SPLIT_LINE) {
            System.err.printf("Пропущена битая строка, т.к. передано неверное количество данных: %s/%s\n",
                    splitLogLine.length, VALID_LENGTH_SPLIT_LINE);
            return Optional.empty();
        }

        final int START_IND = 0;
        final int FINISH_IND = 1;
        final int QUALITY_IND = 2;

        try {
            LocalDateTime start = LocalDateTime.parse(splitLogLine[START_IND], SleepSession.FORMATTER);
            LocalDateTime finish = LocalDateTime.parse(splitLogLine[FINISH_IND], SleepSession.FORMATTER);
            SleepQuality quality = SleepQuality.valueOf(splitLogLine[QUALITY_IND]);

            return Optional.of(new SleepSession(start, finish, quality));
        } catch (DateTimeParseException | IllegalArgumentException e) {
            throw new SessionParseException("Invalid values during parsing: " + sleepLogLine, e);
        }
    }
}
