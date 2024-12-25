package ru.practicum.ewm.base.enums;

import java.util.Optional;

public enum States {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static Optional<States> from(String stringState) {
        for (States state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
