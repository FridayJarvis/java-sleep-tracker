package ru.yandex.practicum.sleeptracker.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.DTO.DateTimeInterval;
import ru.yandex.practicum.sleeptracker.DTO.SleepQuality;
import ru.yandex.practicum.sleeptracker.DTO.SleepSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassifyUserFunctionTest {
    static final ClassifyUserFunction function = new ClassifyUserFunction();
    List<SleepSession> sessions;

    @Test
    @DisplayName("Пустой список записей сна")
    void shouldReturnDoveWithEmptySessions() {
        sessions = List.of();
        assertEquals("Классификация пользователя по типу сна: DOVE", function.apply(sessions));

        sessions = null;
        assertEquals("Классификация пользователя по типу сна: DOVE", function.apply(sessions));
    }

    @Test
    @DisplayName("Пользователь Сова, засыпание после 23:00, пробуждение после 9:00")
    void shouldReturnOwlWithLateSleepAndLateWake() {
        sessions = List.of(
                new SleepSession(//сессия со стартом после 23:00 и концом после 09:00
                        LocalDateTime.parse("01.10.25 23:30", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.10.25 09:30", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD),
                new SleepSession( //еще одна ночь типа "сова", даже если уснул за полночь
                        LocalDateTime.parse("03.10.25 01:00", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("03.10.25 10:00", DateTimeInterval.FORMATTER),
                        SleepQuality.NORMAL));
        assertEquals("Классификация пользователя по типу сна: OWL", function.apply(sessions)); // 2 ночи совы
    }

    @Test
    @DisplayName("Пользователь Жаворонок, засыпание до 22:00, пробуждение до 7:00")
    void shouldReturnLarkWithEarlySleepAndEarlyWake() {
        sessions = List.of(
                new SleepSession(//сессия со стартом до 22:00 и концом до 07:00
                        LocalDateTime.parse("01.10.25 21:30", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.10.25 06:30", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD));
        assertEquals("Классификация пользователя по типу сна: LARK", function.apply(sessions));
    }

    @Test
    @DisplayName("Пользователь Голубь, пограничные и смешанные графики")
    void shouldReturnDoveWithNormalSleep() {
        sessions = List.of(
                new SleepSession(//уснул как сова, но проснулся до 09:00, поэтому голубь
                        LocalDateTime.parse("01.10.25 23:30", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.10.25 08:00", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD));
        assertEquals("Классификация пользователя по типу сна: DOVE", function.apply(sessions));

        sessions = List.of(
                new SleepSession(//уснул как жаворонок, но проспал дольше 07:00, поэтому голубь
                        LocalDateTime.parse("01.10.25 21:00", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.10.25 08:00", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD));
        assertEquals("Классификация пользователя по типу сна: DOVE", function.apply(sessions));
    }

    @Test
    @DisplayName("Разрешение ничьей, равное количество ночей разных типов")
    void shouldReturnDoveOnTie() {
        sessions = List.of(
                new SleepSession(//1 ночь сова
                        LocalDateTime.parse("01.10.25 23:30", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.10.25 09:30", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD),
                new SleepSession(//1 ночь жаворонок
                        LocalDateTime.parse("02.10.25 21:30", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("03.10.25 06:30", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD));
        assertEquals("Классификация пользователя по типу сна: DOVE", function.apply(sessions)); /*1 сова и 1 жаворонок, поэтому
        голубь*/
    }

    @Test
    @DisplayName("Игнорирование дневного сна и фантомных сессий")
    void shouldIgnoreDayAndPhantomSessions() {
        sessions = List.of(
                new SleepSession(//Дневной сон, жаворонок по времени, но не пересекает 00:00 - 06:00
                        LocalDateTime.parse("01.10.25 14:00", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("01.10.25 16:00", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD),
                new SleepSession(//Фантомная сессия, длительность 0, сова по времени, игнорируем
                        LocalDateTime.parse("01.10.25 23:30", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("01.10.25 23:30", DateTimeInterval.FORMATTER),
                        SleepQuality.BAD),
                new SleepSession(//Единственная реальная ночная сессия (Голубь)
                        LocalDateTime.parse("01.10.25 22:30", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.10.25 07:30", DateTimeInterval.FORMATTER),
                        SleepQuality.NORMAL));

        assertEquals("Классификация пользователя по типу сна: DOVE", function.apply(sessions)); /*2 сессии отброшены, осталась
        только 1*/
    }
}