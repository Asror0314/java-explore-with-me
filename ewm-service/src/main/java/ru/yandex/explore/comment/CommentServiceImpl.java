package ru.yandex.explore.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.explore.comment.dto.CommentDto;
import ru.yandex.explore.comment.dto.MapperParam;
import ru.yandex.explore.comment.dto.NewCommentDto;
import ru.yandex.explore.event.Event;
import ru.yandex.explore.event.EventService;
import ru.yandex.explore.event.dto.EventState;
import ru.yandex.explore.exception.EditRulesException;
import ru.yandex.explore.exception.NotFoundException;
import ru.yandex.explore.user.User;
import ru.yandex.explore.user.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final UserService userService;
    private final EventService eventService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String datetime = LocalDateTime.now().format(formatter);
    private final LocalDateTime createdOn = LocalDateTime.parse(datetime, formatter);

    @Override
    @Transactional
    public CommentDto addNewComment(NewCommentDto newComDto, Long authorId, Long eventId) {
        final User author = userService.findUserById(authorId);
        final Event event = eventService.findEventById(eventId);
        validPublicEvent(event);

        final MapperParam mapperParam = new MapperParam();
        final MapperParam paramBuild = mapperParam
                .setCommentDto(newComDto)
                .setAuthor(author)
                .setEvent(event)
                .setCreatedOn(createdOn)
                .build();

        final Comment comment = CommentMapper.mapToComment(paramBuild);
        final Comment addedComment = repository.save(comment);

        return CommentMapper.mapToCommentDto(addedComment);
    }

    @Override
    @Transactional
    public void deleteCommentAdmin(Long commentId) {
        findCommentById(commentId);
        repository.deleteById(commentId);
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        final Comment comment = findCommentById(commentId);
        return CommentMapper.mapToCommentDto(comment);
    }

    @Override
    public List<CommentDto> getAllCommentsEvent(Long eventId, int from, int size) {
        eventService.findEventById(eventId);
        Pageable page = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        final List<Comment> comments = repository.findCommentsByEvent_Id(eventId, page);

        return comments
                .stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public Comment findCommentById(Long commentId) {
        return repository.findById(commentId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Comment with id = %d was not found", commentId)));
    }

    private void validPublicEvent(Event event) {
        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new EditRulesException("Only published events can be creat a comment");
        }
    }
}
