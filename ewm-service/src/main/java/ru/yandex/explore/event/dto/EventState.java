package ru.yandex.explore.event.dto;

import java.util.Optional;

public enum EventState {
    PENDING, PUBLISHED, CONFIRMED, REJECTED;

    public static Optional<EventState> from(String stringState) {
        for (EventState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
