package ru.yandex.practicum.sleeptracker.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.yandex.practicum.sleeptracker.dto.SleepSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SleepLogsTest {
    @TempDir
    Path tempDir;

    Path tempSleepLog;

    @BeforeEach
    void setUpSleepLog() {
        tempSleepLog = tempDir.resolve("sleepLog.txt");
    }

    @Test
    @DisplayName("Загрузка пустого лога")
    void shouldReturnEmptyList() throws IOException {
        Files.createFile(tempSleepLog);

        List<SleepSession> sleepSessions = SleepLogs.load(tempSleepLog);
        assertTrue(sleepSessions.isEmpty());
    }

    @Test
    @DisplayName("Загрузка корректных записей")
    void shouldLoad2CorrectSessions() throws IOException {
        String content = """
                01.10.25 22:15;02.10.25 08:00;GOOD
                02.10.25 23:00;03.10.25 08:00;NORMAL""";
        Files.writeString(tempSleepLog, content);

        List<SleepSession> sleepSessions = SleepLogs.load(tempSleepLog);
        assertEquals(2, sleepSessions.size());
    }

    @Test
    @DisplayName("Отфильтровка одинаковых записей")
    void shouldLoad2SessionAndSkipContainedYet() throws IOException {
        String content = """
                01.10.25 22:15;02.10.25 08:00;GOOD
                03.10.25 23:40;04.10.25 08:00;BAD
                01.10.25 22:15;02.10.25 08:00;GOOD""";

        Files.writeString(tempSleepLog, content);

        List<SleepSession> sleepSessions = SleepLogs.load(tempSleepLog);
        assertEquals(2, sleepSessions.size());
    }

    @Test
    @DisplayName("Отфильтровка невалидных непустых записей")
    void shouldSkipNotEmptyInvalidLogsAndLoad1Valid() throws IOException {
        String content =
                "01.10.25 23:15;02.10.25 07:30\n" +           //не хватает значения
                "02.10.25 23:50;03.10.25 06:40;NORMAL;gg\n" + //значений слишком много
                "02.10.25 23:50;03.10.25 06:40;NORMAL;\n" +   //Валидная запись
                "3.10.25 14:10;03.10.25 15:00;NORMAL\n" +     //неправильный формат даты начала
                "33.10.25 23:40;04.10.25 08:00;BAD\n" +       //неправильный формат даты начала
                "05.10.25 90:10;05.10.25 06:20;GOOD\n" +      //неправильный формат времени начала
                "05.10.25 а 13:30;05.10.25 14:15;NORMAL\n" +  //посторонний символ между датой и временем начала
                "06.10.25 22:30;07.10.25 05:50;\n" +          //неправильный формат качества сна
                "07.10.25 23:45;08.10.25 06:30;G\n";          //неправильный формат качества сна

        Files.writeString(tempSleepLog, content);

        List<SleepSession> sleepSessions = SleepLogs.load(tempSleepLog);
        assertEquals(1, sleepSessions.size());
    }

    @Test
    @DisplayName("Отфильтровка пустых записей")
    void shouldSkipEmptyLogsAndLoad1Valid() throws IOException {
        String content = "\n\n01.10.25 23:15;02.10.25 07:30;GOOD"; //2 пустых строки и валидная запись
        Files.writeString(tempSleepLog, content);

        List<SleepSession> sleepSessions = SleepLogs.load(tempSleepLog);
        assertEquals(1, sleepSessions.size());
    }

    @Test
    @DisplayName("Отфильтровка разных невалидных записей и в итоге возврат пустого списка")
    void shouldSkepInvalidLogsAndReturnEmptyList() throws IOException {
        String content =
                "01.10.25 23:15;02.10.25 07:30\n" +           //не хватает значения
                "02.10.25 23:50;03.10.25 06:40;NORMAL;gg\n" + //значений слишком много
                "3.10.25 14:10;03.10.25 15:00;NORMAL\n" +     //неправильный формат даты начала
                "33.10.25 23:40;04.10.25 08:00;BAD\n" +       //неправильный формат даты начала
                "05.10.25 90:10;05.10.25 06:20;GOOD\n" +      //неправильный формат времени начала
                "05.10.25 а 13:30;05.10.25 14:15;NORMAL\n" +  //посторонний символ между датой и временем начала
                "06.10.25 22:30;07.10.25 05:50;\n" +          //неправильный формат качества сна
                "07.10.25 23:45;08.10.25 06:30;G\n";          //неправильный формат качества сна

        Files.writeString(tempSleepLog, content);

        List<SleepSession> sleepSessions = SleepLogs.load(tempSleepLog);
        assertTrue(sleepSessions.isEmpty());
    }
}