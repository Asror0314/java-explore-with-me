package ru.yandex.explore.comment;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.explore.event.Event;
import ru.yandex.explore.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(schema = "explore", name = "comment")
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text")
    private String text;
    @Column(name = "created_date")
    private LocalDateTime createdOn;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
}
