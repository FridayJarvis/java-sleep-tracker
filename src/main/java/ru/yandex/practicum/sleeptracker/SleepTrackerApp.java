package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.Exception.SessionParseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SleepTrackerApp {
    public static void main(String[] args) {
            List<SleepSession> sleepSessions = SleepLogs.load("SleepLogs.txt");
            System.out.println(sleepSessions);
    }
}