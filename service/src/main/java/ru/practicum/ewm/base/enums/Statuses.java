package ru.practicum.ewm.base.enums;

import java.util.Optional;

public enum Statuses {
    CONFIRMED,
    REJECTED,
    PENDING,
    CANCELED;

    public static Optional<Statuses> from(String stringState) {
        for (Statuses status : values()) {
            if (status.name().equalsIgnoreCase(stringState)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}