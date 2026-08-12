package ru.yandex.practicum.sleeptracker.function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.dto.DateTimeInterval;
import ru.yandex.practicum.sleeptracker.dto.SleepQuality;
import ru.yandex.practicum.sleeptracker.dto.SleepSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaxSessionByMinutesFunctionTest {
    static final MaxSessionByMinutesFunction function = new MaxSessionByMinutesFunction();
    List<SleepSession> sessions;

    @Test
    @DisplayName("Непустой список записей сна")
    void testApplyWithNotEmptySessionList() {
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
                        SleepQuality.BAD));
        assertEquals("""
                Максимальная сессия сна:
                \t- начало: 03.10.25 23:40
                \t- конец: 04.10.25 08:00
                \t- качество: BAD
                \t- продолжительность: 500 минут""", function.apply(sessions));
    }

    @Test
    @DisplayName("Пустой список записей сна")
    void testApplyWithEmptySessionList() {
        assertEquals("Максимальная сессия сна: 0 минут", function.apply(List.of()));
    }

}