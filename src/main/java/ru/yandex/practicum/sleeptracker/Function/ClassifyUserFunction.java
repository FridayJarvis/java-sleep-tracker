package ru.yandex.practicum.sleeptracker.Function;

import ru.yandex.practicum.sleeptracker.DTO.SleepClassification;
import ru.yandex.practicum.sleeptracker.DTO.SleepSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ClassifyUserFunction implements Function<List<SleepSession>, String> {
    @Override
    public String apply(List<SleepSession> sessions) {
        if (sessions == null) {
            return "Классификация пользователя по типу сна: " + SleepClassification.DOVE;
        }

        List<SleepClassification> classificationsList = sessions.stream()
                .filter(Objects::nonNull)
                .filter(session -> session.start().isBefore(session.end()))
                .filter(this::isNightSession)
                .map(this::classifySession)
                .toList();

        long owls = classificationsList.stream()
                .filter(classification -> classification == SleepClassification.OWL)
                .count();
        long larks = classificationsList.stream()
                .filter(classifySession -> classifySession == SleepClassification.LARK)
                .count();
        long doves = classificationsList.size() - owls - larks;

        SleepClassification result;
        if (owls > larks && owls > doves) {
            result = SleepClassification.OWL;
        } else if (larks > owls && larks > doves) {
            result = SleepClassification.LARK;
        } else {
            result = SleepClassification.DOVE;
        }

        return "Классификация пользователя по типу сна: " + result;
    }

    private boolean isNightSession(SleepSession session) {
        LocalDate targetNight = session.start().toLocalDate();
        if (session.start().getHour() >= 12) {
            targetNight = targetNight.plusDays(1);
        }

        LocalDateTime startNight = LocalDateTime.of(targetNight, LocalTime.MIDNIGHT);
        LocalDateTime endNight = LocalDateTime.of(targetNight, LocalTime.of(6, 0));

        return session.start().isBefore(endNight) && session.end().isAfter(startNight);
    }

    private SleepClassification classifySession(SleepSession session) {
        LocalDate targetNight = session.start().toLocalDate();
        if (session.start().getHour() >= 12) {
            targetNight = targetNight.plusDays(1);
        }

        LocalDateTime owlStartLimit = LocalDateTime.of(targetNight, LocalTime.of(23, 0));
        LocalDateTime owlEndLimit = LocalDateTime.of(targetNight, LocalTime.of(9, 0));

        LocalDateTime larkStartLimit = LocalDateTime.of(targetNight, LocalTime.of(22, 0));
        LocalDateTime larkEndLimit = LocalDateTime.of(targetNight, LocalTime.of(7, 0));

        if (session.start().isAfter(owlStartLimit) && session.end().isBefore(owlEndLimit)) {
            return SleepClassification.OWL;
        } else if (session.start().isAfter(larkStartLimit) && session.end().isBefore(larkEndLimit)) {
            return SleepClassification.LARK;
        } else {
            return SleepClassification.DOVE;
        }
    }
}
