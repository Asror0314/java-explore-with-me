package ru.yandex.explore.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.yandex.explore.event.Event;
import ru.yandex.explore.user.User;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MapperParam {
    private NewCommentDto commentDto;
    private User author;
    private Event event;
    private LocalDateTime createdOn;

    public MapperParam setCommentDto(NewCommentDto commentDto) {
        this.commentDto = commentDto;
        return this;
    }

    public MapperParam setAuthor(User author) {
        this.author = author;
        return this;
    }

    public MapperParam setEvent(Event event) {
        this.event = event;
        return this;
    }

    public MapperParam setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public MapperParam build() {
        return new MapperParam(commentDto, author, event, createdOn);
    }
}
