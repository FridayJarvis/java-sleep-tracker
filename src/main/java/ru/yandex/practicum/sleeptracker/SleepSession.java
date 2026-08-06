package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SleepSession {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    LocalDateTime start;
    LocalDateTime finish;

    SleepQuality sleepQuality;

    public SleepSession(LocalDateTime start, LocalDateTime finish, SleepQuality sleepQuality) {
        this.start = start;
        this.finish = finish;
        this.sleepQuality = sleepQuality;
    }

    @Override
    public String toString() {
        return "SleepSession{" +
                "start=" + start.format(FORMATTER) +
                ", finish=" + finish.format(FORMATTER) +
                ", sleepQuality=" + sleepQuality +
                '}';
    }
}
