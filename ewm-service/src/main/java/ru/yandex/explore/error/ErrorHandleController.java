package ru.yandex.explore.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.explore.exception.EditRulesException;
import ru.yandex.explore.exception.NotFoundException;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class ErrorHandleController {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDateTimeParse(final DateTimeParseException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(final ValidationException e) {
        return new ErrorResponse("BAD_REQUEST",
                "Incorrectly made request",
                e.getMessage(), LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        return new ErrorResponse("NOT_FOUND",
                "For the requested operation the conditions are not met",
                e.getMessage(), LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEditRules(final EditRulesException e) {
        return new ErrorResponse("FORBIDDEN",
                "For the requested operation the conditions are not met",
                e.getMessage(), LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }
}
