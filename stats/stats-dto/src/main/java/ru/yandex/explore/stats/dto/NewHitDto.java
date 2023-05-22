package ru.yandex.explore.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class NewHitDto {
    @NotNull
    String app;
    @NotNull
    String uri;
    @NotNull
    String ip;
    @NotNull
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp;
}
