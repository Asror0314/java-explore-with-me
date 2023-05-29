package ru.yandex.explore.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.explore.comment.dto.CommentDto;
import ru.yandex.explore.comment.dto.NewCommentDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentController {
    private final CommentService service;

    @PostMapping("users/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    CommentDto addNewComment(
            @Valid @RequestBody NewCommentDto newComDto,
            @PathVariable(name = "userId") Long authorId,
            @RequestParam(name = "eventId") Long eventId
    ) {
        log.info("Creating new comment with authorId={}, eventId={}", authorId, eventId);
        return service.addNewComment(newComDto, authorId, eventId);
    }

    @DeleteMapping("/admin/comments/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCommentAdmin(@PathVariable(name = "comId") Long commentId) {
        log.info("Delete comment with commentId={}", commentId);
        service.deleteCommentAdmin(commentId);
    }

    @GetMapping("/comments/{comId}")
    CommentDto getCommentById(@PathVariable(name = "comId") Long commentId) {
        log.info("Get comment with commentId={}", commentId);
        return service.getCommentById(commentId);
    }

    @GetMapping("/comments")
    List<CommentDto> getAllCommentsEvent(
            @RequestParam(name = "eventId") Long eventId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Get all comments with eventId={}, from={}, size={}", eventId, from, size);
        return service.getAllCommentsEvent(eventId, from, size);
    }

}
