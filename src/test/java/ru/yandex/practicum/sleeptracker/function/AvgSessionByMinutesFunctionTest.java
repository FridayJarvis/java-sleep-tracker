package ru.yandex.practicum.sleeptracker.function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.dto.DateTimeInterval;
import ru.yandex.practicum.sleeptracker.dto.SleepQuality;
import ru.yandex.practicum.sleeptracker.dto.SleepSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AvgSessionByMinutesFunctionTest {
    static final AvgSessionByMinutesFunction function = new AvgSessionByMinutesFunction();
    List<SleepSession> sessions;

    @Test
    @DisplayName("Пустой список записей сна")
    void testApplyWithEmptySessionList() {
        assertEquals("Средняя сессия сна: 0.00 минут", function.apply(List.of()));
        assertEquals("Средняя сессия сна: 0.00 минут", function.apply(null));
    }

    @Test
    @DisplayName("1 запись сна")
    void testApplyWith1Log() {
        sessions = List.of(new SleepSession(
                LocalDateTime.parse("01.10.25 23:15", DateTimeInterval.FORMATTER),
                LocalDateTime.parse("02.10.25 07:30", DateTimeInterval.FORMATTER),
                SleepQuality.GOOD));
        assertEquals("Средняя сессия сна: 495.00 минут", function.apply(sessions));
    }

    @Test
    @DisplayName("Больше одной записи сна")
    void testApplyWithManyLogs() {
        sessions = List.of(
                new SleepSession(
                        LocalDateTime.parse("01.10.25 23:15", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.10.25 07:30", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD),
                new SleepSession(
                        LocalDateTime.parse("03.10.25 14:10", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("03.10.25 15:00", DateTimeInterval.FORMATTER),
                        SleepQuality.NORMAL),
                new SleepSession(
                        LocalDateTime.parse("03.10.25 23:40", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("04.10.25 08:00", DateTimeInterval.FORMATTER),
                        SleepQuality.BAD),
                new SleepSession(
                        LocalDateTime.parse("05.10.25 00:10", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("05.10.25 06:20", DateTimeInterval.FORMATTER),
                        SleepQuality.BAD));
        assertEquals("Средняя сессия сна: 353.75 минут", function.apply(sessions));
    }
}