package ru.yandex.explore.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH-dd-ss")
    private LocalDateTime createdOn;
    private Long event;
    private Long author;

    public CommentDto setId(Long id) {
        this.id = id;
        return this;
    }

    public CommentDto setText(String text) {
        this.text = text;
        return this;
    }

    public CommentDto setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public CommentDto setEvent(Long event) {
        this.event = event;
        return this;
    }

    public CommentDto setAuthor(Long author) {
        this.author = author;
        return this;
    }

    public CommentDto build() {
        return new CommentDto(id, text, createdOn, event, author);
    }
}
