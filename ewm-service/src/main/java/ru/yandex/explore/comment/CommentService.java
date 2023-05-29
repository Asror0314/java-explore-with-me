package ru.yandex.explore.comment;

import ru.yandex.explore.comment.dto.CommentDto;
import ru.yandex.explore.comment.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addNewComment(NewCommentDto newComDto, Long authorId, Long eventId);

    void deleteCommentAdmin(Long commentId);

    CommentDto getCommentById(Long commentId);

    List<CommentDto> getAllCommentsEvent(Long eventId, int from, int size);

    Comment findCommentById(Long commentId);
}
