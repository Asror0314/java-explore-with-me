package ru.yandex.explore.event.dto;

import java.util.Optional;

public enum EventStateAction {
    SEND_TO_REVIEW, PUBLISH_EVENT, CANCEL_REVIEW, REJECT_EVENT;

    public static Optional<EventStateAction> from(String stringState) {
        for (EventStateAction state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
