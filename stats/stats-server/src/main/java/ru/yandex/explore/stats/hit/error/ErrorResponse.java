package ru.yandex.explore.stats.hit.error;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String status;
    private String reason;
    private String message;
    private String timestamp;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(String status, String reason, String message, String timestamp) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
    }
}
