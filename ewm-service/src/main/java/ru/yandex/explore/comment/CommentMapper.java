package ru.yandex.explore.comment;

import ru.yandex.explore.comment.dto.CommentDto;
import ru.yandex.explore.comment.dto.MapperParam;

public class CommentMapper {
    public static Comment mapToComment(MapperParam param) {
        final Comment comment = new Comment();
        comment.setText(param.getCommentDto().getText());
        comment.setCreatedOn(param.getCreatedOn());
        comment.setAuthor(param.getAuthor());
        comment.setEvent(param.getEvent());

        return comment;
    }

    public static CommentDto mapToCommentDto(Comment comment) {
        final CommentDto commentBuilder = new CommentDto();

        commentBuilder
                .setId(comment.getId())
                .setText(comment.getText())
                .setCreatedOn(comment.getCreatedOn())
                .setAuthor(comment.getAuthor().getId())
                .setEvent(comment.getEvent().getId())
                .build();

        return commentBuilder;
    }
}
