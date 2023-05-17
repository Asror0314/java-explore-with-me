package ru.yandex.explore.request;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.explore.event.Event;
import ru.yandex.explore.event.dto.EventState;
import ru.yandex.explore.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(schema = "explore", name = "participation")
@Getter
@Setter
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created")
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    @Enumerated(EnumType.STRING)
    private EventState status;
}
