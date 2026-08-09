package ru.yandex.practicum.sleeptracker.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.DTO.SleepQuality;
import ru.yandex.practicum.sleeptracker.DTO.SleepSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AvgSessionByMinutesFunctionTest {
    static final AvgSessionByMinutesFunction function = new AvgSessionByMinutesFunction();
    List<SleepSession> sessions;

    @Test
    @DisplayName("Работа функции с пустым списком")
    void testApplyWithEmptySessionList() {
        assertEquals("Средняя сессия сна: 0.00 минут", function.apply(List.of()));
    }

    @Test
    @DisplayName("Работа функции с 1 записью")
    void testApplyWith1Log() {
        sessions = List.of(new SleepSession(
                LocalDateTime.parse("01.10.25 23:15", SleepSession.FORMATTER),
                LocalDateTime.parse("02.10.25 07:30", SleepSession.FORMATTER),
                SleepQuality.GOOD));
        assertEquals("Средняя сессия сна: 495.00 минут", function.apply(sessions));
    }

    @Test
    @DisplayName("Работа функции c множеством записей")
    void testApplyWithManyLogs() {
        sessions = List.of(
                new SleepSession(
                        LocalDateTime.parse("01.10.25 23:15", SleepSession.FORMATTER),
                        LocalDateTime.parse("02.10.25 07:30", SleepSession.FORMATTER),
                        SleepQuality.GOOD),
                new SleepSession(
                        LocalDateTime.parse("03.10.25 14:10", SleepSession.FORMATTER),
                        LocalDateTime.parse("03.10.25 15:00", SleepSession.FORMATTER),
                        SleepQuality.NORMAL),
                new SleepSession(
                        LocalDateTime.parse("03.10.25 23:40", SleepSession.FORMATTER),
                        LocalDateTime.parse("04.10.25 08:00", SleepSession.FORMATTER),
                        SleepQuality.BAD),
                new SleepSession(
                        LocalDateTime.parse("05.10.25 00:10", SleepSession.FORMATTER),
                        LocalDateTime.parse("05.10.25 06:20", SleepSession.FORMATTER),
                        SleepQuality.BAD));
        assertEquals("Средняя сессия сна: 353.75 минут", function.apply(sessions));
    }
}