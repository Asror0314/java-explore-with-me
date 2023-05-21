package ru.yandex.explore.event;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.explore.category.Category;
import ru.yandex.explore.event.dto.EventState;
import ru.yandex.explore.location.Location;
import ru.yandex.explore.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(schema = "explore", name = "event")
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "annotation")
    private String annotation;
    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "confirmed_requests")
    private int confirmedRequests;
    @Column(name = "created_date")
    private LocalDateTime createdOn;
    @Column(name = "description")
    private String description;
    @Column(name = "eventDate")
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @Column(name = "paid")
    private boolean paid;
    @Column(name = "participant_limit")
    private int participantLimit;
    @Column(name = "published_date")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @Column(name = "title")
    private String title;
    @Column(name = "view")
    private int view;
    @Column(name = "limit_available")
    private boolean limitAvailable;
}
