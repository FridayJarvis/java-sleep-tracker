package ru.yandex.practicum.sleeptracker.IO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.yandex.practicum.sleeptracker.DTO.SleepSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SleepLogsTest {
    @TempDir
    Path tempSleepLog;

    @BeforeEach
    void initTempDir() {
        tempSleepLog = Path.of("sleepLogs.txt");
    }

    @AfterEach
    void deleteTempDir() throws IOException {
        Files.deleteIfExists(tempSleepLog);
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
    @DisplayName("Пропуск одинаковых записей")
    void shouldLoad2SessionAndSkipContainedYet() throws IOException {
        String content = """
                01.10.25 22:15;02.10.25 08:00;GOOD
                03.10.25 23:40;04.10.25 08:00;BAD
                01.10.25 22:15;02.10.25 08:00;GOOD""";

        Files.writeString(tempSleepLog, content);

        List<SleepSession> sleepSessions = SleepLogs.load(tempSleepLog);
        assertEquals(2, sleepSessions.size());
    }


}