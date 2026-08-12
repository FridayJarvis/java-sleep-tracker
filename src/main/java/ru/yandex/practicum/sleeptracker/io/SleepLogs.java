package ru.yandex.practicum.sleeptracker.io;

import ru.yandex.practicum.sleeptracker.dto.DateTimeInterval;
import ru.yandex.practicum.sleeptracker.dto.SleepQuality;
import ru.yandex.practicum.sleeptracker.dto.SleepSession;
import ru.yandex.practicum.sleeptracker.exception.SessionParseException;

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

    public static List<SleepSession> load(final Path sleepLogPath) {
        try (final BufferedReader reader = Files.newBufferedReader(sleepLogPath, StandardCharsets.UTF_8)) {
            return reader.lines()
                    .map(SleepLogs::parseSleepLogLine)
                    .flatMap(Optional::stream)
                    .filter(session -> session.start().isBefore(session.end()))
                    .distinct()
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Поток ввода не открылся по какой-то причине", e);
        }
    }

    private static Optional<SleepSession> parseSleepLogLine(final String sleepLogLine) {
        if (sleepLogLine.isBlank()) {
            return Optional.empty();
        }

        String[] splitLogLine = sleepLogLine.split(";");

        final int VALID_LENGTH_SPLIT_LINE = 3;
        if (splitLogLine.length != VALID_LENGTH_SPLIT_LINE) {
            return Optional.empty();
        }

        final int START_IND = 0;
        final int FINISH_IND = 1;
        final int QUALITY_IND = 2;

        try {
            LocalDateTime start;
            LocalDateTime end;
            SleepQuality quality;
            try {
                start = LocalDateTime.parse(splitLogLine[START_IND], DateTimeInterval.FORMATTER);
                end = LocalDateTime.parse(splitLogLine[FINISH_IND], DateTimeInterval.FORMATTER);

                quality = SleepQuality.valueOf(splitLogLine[QUALITY_IND]);

                return Optional.of(new SleepSession(start, end, quality));
            } catch (DateTimeParseException | IllegalArgumentException e) {
                throw new SessionParseException();
            }
        } catch (SessionParseException e) {
            return Optional.empty();
        }
    }
}
