package ru.yandex.practicum.sleeptracker.function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.dto.DateTimeInterval;
import ru.yandex.practicum.sleeptracker.dto.SleepQuality;
import ru.yandex.practicum.sleeptracker.dto.SleepSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SleeplessNightsFunctionTest {
    static final SleeplessNightsFunction function = new SleeplessNightsFunction();
    List<SleepSession> sessions;

    @Test
    @DisplayName("Пустой список записей сна")
    void shouldReturn0SleeplessNightsWithEmptySessions() {
        sessions = List.of();
        assertEquals("Бессонных ночей: 0", function.apply(sessions));

        sessions = null;
        assertEquals("Бессонных ночей: 0", function.apply(sessions));
    }

    @Test
    @DisplayName("Фантомные записи сна")
    void shouldReturn0SleeplessNightsWithPhantomsSessions() {
        sessions = List.of(
                new SleepSession(//сессия со стартом от 06:00 до 12:00 с длительностью 0 секунд
                        LocalDateTime.parse("01.10.25 11:59", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("01.10.25 11:59", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD),
                new SleepSession(//отрицательная длительность с началом от 06:00 до 12:00
                        LocalDateTime.parse("01.10.25 11:59", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("01.10.25 11:58", DateTimeInterval.FORMATTER),
                        SleepQuality.NORMAL),
                new SleepSession(//сессия со стартом от 12:00 до 00:00 с длительностью 0 секунд
                        LocalDateTime.parse("01.10.25 12:00", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("01.10.25 12:00", DateTimeInterval.FORMATTER),
                        SleepQuality.BAD),
                new SleepSession(//отрицательная длительность с началом от 12:00 до 00:00
                        LocalDateTime.parse("01.10.25 12:01", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("01.10.25 12:00", DateTimeInterval.FORMATTER),
                        SleepQuality.BAD));
        assertEquals("Бессонных ночей: 0", function.apply(sessions));
    }

    @Test
    @DisplayName("1 запись сна с началом от 06:00 до 12:00 и концом, начиная от старта до +∞")
    void test1SessionWithStartAfter6AndBefore12() {
        sessions = List.of(
                new SleepSession(
                        LocalDateTime.parse("01.10.25 06:00", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.10.25 00:00", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD));
        assertEquals("Бессонных ночей: 1", function.apply(sessions));
    }

    @Test
    @DisplayName("1 запись сна с началом от 12:00 до 00:00")
    void test1SessionWithStartAfter12AndBefore24() {
        sessions = List.of(
                new SleepSession(//сессия со стартом от 12:00 до 00:00 и концом от 12:00 до 00:00
                        LocalDateTime.parse("01.10.25 12:00", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.10.25 00:00", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD));
        assertEquals("Бессонных ночей: 1", function.apply(sessions)); // 1 бессонная ночь, т.к. начало от 12:00, а конец до 00:00,
        //хотя бы 1 секунда сна должна выпасть на интервал от 00:00 до 06:00

        sessions = List.of(
                new SleepSession(//сессия со стартом от 12:00 до 00:00 и концом от 00:00 до +∞
                        LocalDateTime.parse("01.10.25 12:00", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.10.25 00:01", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD));
        assertEquals("Бессонных ночей: 0", function.apply(sessions)); // 0 бессонных ночей, т.к. сон с 00:00 до 00:01 составил 1 с.
    }

    @Test
    @DisplayName("2 сессии с со стартом и концом от 06:00 до 12:00")
    void shouldReturn1SleeplessNightWith2SessionAfter6AndBefore12() {
        sessions = List.of(
                new SleepSession(
                        LocalDateTime.parse("01.10.25 11:58", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("01.10.25 11:59", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD),
                new SleepSession(
                        LocalDateTime.parse("01.10.25 11:59", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("01.10.25 12:00", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD));
        assertEquals("Бессонных ночей: 1", function.apply(sessions)); /*1 ночь, т.к. у обоих ночей начало до 12:00, поэтому
        потенциальной ночью для сна является предыдущая*/
    }

    @Test
    @DisplayName("2 сессии с со стартом и концом от 12:00 до 00:00")
    void shouldReturn1SleeplessNightWith2SessionAfter12AndBefore24() {
        sessions = List.of(
                new SleepSession(
                        LocalDateTime.parse("01.10.25 12:00", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("01.10.25 12:01", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD),
                new SleepSession(
                        LocalDateTime.parse("01.10.25 12:01", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.10.25 00:00", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD));
        assertEquals("Бессонных ночей: 1", function.apply(sessions)); /*1 ночь, т.к. у обоих ночей начало после 12:00, поэтому
        потенциальной ночью для сна является следующая*/
    }

    @Test
    @DisplayName("2 сессии: одна со стартом в интервале от 06:00 до 12:00, другая от 12:00 до 00:00")
    void shouldReturn2SleeplessNightsOneAfter6Before12AndOneAfter12Before24() {
        sessions = List.of(
                new SleepSession(//сон со стартом в интервале от 06:00 до 12:00
                        LocalDateTime.parse("01.10.25 11:59", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("01.10.25 12:00", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD),
                new SleepSession(
                        LocalDateTime.parse("01.10.25 12:00", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("01.10.25 12:01", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD));

        assertEquals("Бессонных ночей: 2", function.apply(sessions)); /*2 бессонные ночи, т.к. у 1-ой старт до 12:00, а значит
        потенциальная ночь для сна предыдущая (+1), а у 2-ой старт после 12:00, поэтому потенциальная ночь для сна следующая (+1)*/
    }

    @Test
    @DisplayName("2 сессии: вторая через сутки после первой")
    void test2SessionsFirstIsDayAfterSecond() {
        //Сессии, у которых старт первого сна в интервале от 06:00 до 12:00 (+1 к бессонным)
        sessions = List.of(
                new SleepSession(
                        LocalDateTime.parse("01.10.25 11:59", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("01.10.25 12:00", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD),
                new SleepSession(//сон через день после конца 1ого, начало 2ого сна до 06:00
                        LocalDateTime.parse("02.10.25 05:59", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.10.25 12:01", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD));
        assertEquals("Бессонных ночей: 1", function.apply(sessions)); /*1т.к. старт первой в интервале от 06:00 до 12:00 (+1), а
        второй сон задевает интервал с 00:00 до 06:00 на 1 секунду (сон в этом интервале длился 1 секунду, значит ночь не считается бессонной)*/

        sessions = List.of(
                new SleepSession(
                        LocalDateTime.parse("01.10.25 11:59", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("01.10.25 12:00", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD),
                new SleepSession(//сон через день после конца 1ого, начало 2ого сна от 06:00
                        LocalDateTime.parse("02.10.25 06:00", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.10.25 11:59", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD));
        assertEquals("Бессонных ночей: 2", function.apply(sessions)); /*2т.к. старт первой в интервале от 06:00 до 12:00 (+1), а второй в
        интервале от 06:00, поэтому предыдущая ночь бессонная (+1)*/

        //сессии, у которых старт первого сна в интервале от 12:00 до 06:00
        sessions = List.of(
                new SleepSession(
                        LocalDateTime.parse("01.10.25 12:00", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.10.25 00:00", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD),
                new SleepSession(//сон через день после конца 1ого, начало 2ого сна до 06:00
                        LocalDateTime.parse("02.10.25 05:59", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.10.25 12:01", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD));
        assertEquals("Бессонных ночей: 0", function.apply(sessions)); /*т.к. начало 1ого сна после 12, а 2ой на 1 сек. задевает
        интервал от 00:00 до 06:00, то ночь бессонной не считается*/

        sessions = List.of(
                new SleepSession(
                        LocalDateTime.parse("01.10.25 12:00", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.10.25 00:00", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD),
                new SleepSession(//сон через день после конца 1ого, начало 2ого сна от 06:00
                        LocalDateTime.parse("02.10.25 06:00", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.10.25 11:59", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD));
        assertEquals("Бессонных ночей: 1", function.apply(sessions)); /*1, т.к. начало 1ого после 12 ч, заканчивается до "ночи", а
        2ой начинается после "ночи", поэтому ночь считается бессонной (+1)*/
    }

    @Test
    @DisplayName("2 сессии: первая начинается в одном месяце, вторая в следующем")
    void shouldReturn3SleeplessWithFirstSessionStartsBefore12InFirstMothAnd2ndSessionStartsIsDayAfterInNextMonth() {
        sessions = List.of(
                new SleepSession(//сон начинается до 12:00. Интервал логирования начинается в 1ом месяце
                        LocalDateTime.parse("29.02.24 11:59", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("01.03.24 00:00", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD),
                new SleepSession(//сон через день после конца 1ого, начало 2ого сна от 06:00. Интервал логирования заканчивается во 2ом мес.
                        LocalDateTime.parse("02.03.24 06:00", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.03.24 11:59", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD));
        assertEquals("Бессонных ночей: 3", function.apply(sessions)); /*3, т.к. первая сессия начинается до 12:00 (+1), бессонная
         ночь 01.02.24 и второй сон также как и 1ый начинается после 6, но до 12 (+1)*/
    }

    @Test
    @DisplayName("График здорового человека (хочу себе такой же)")
    void shouldReturn0SleeplessNightsWithPerfectSessions() {
        sessions = List.of(
                new SleepSession(
                        LocalDateTime.parse("01.10.25 23:15", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("02.10.25 07:30", DateTimeInterval.FORMATTER),
                        SleepQuality.GOOD),
                new SleepSession(
                        LocalDateTime.parse("02.10.25 22:10", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("03.10.25 07:00", DateTimeInterval.FORMATTER),
                        SleepQuality.NORMAL),
                new SleepSession(
                        LocalDateTime.parse("03.10.25 23:40", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("04.10.25 08:00", DateTimeInterval.FORMATTER),
                        SleepQuality.BAD),
                new SleepSession(
                        LocalDateTime.parse("05.10.25 00:10", DateTimeInterval.FORMATTER),
                        LocalDateTime.parse("05.10.25 06:20", DateTimeInterval.FORMATTER),
                        SleepQuality.BAD));
        assertEquals("Бессонных ночей: 0", function.apply(sessions));
    }
}