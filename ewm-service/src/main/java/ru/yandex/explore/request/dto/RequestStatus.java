package ru.yandex.explore.request.dto;

import java.util.Optional;

public enum RequestStatus {
    PENDING, CONFIRMED, CANCELED, REJECTED, CANCEL_REVIEW;

    public static Optional<RequestStatus> from(String stringStatus) {
        for (RequestStatus status : values()) {
            if (status.name().equalsIgnoreCase(stringStatus)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
