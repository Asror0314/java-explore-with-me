package ru.yandex.explore.event.dto;

import java.util.Optional;

public enum EventStateAction {
    PUBLISH_EVENT, CANCEL_REVIEW;

    public static Optional<EventStateAction> from(String stringState) {
        for (EventStateAction state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
