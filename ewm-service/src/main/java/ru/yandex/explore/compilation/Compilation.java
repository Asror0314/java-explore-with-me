package ru.yandex.explore.compilation;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.explore.event.Event;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "explore", name = "compilation")
@Getter
@Setter
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany()
    @JoinTable(schema = "explore", name = "compilation_event",
    joinColumns = @JoinColumn(name = "compilation_id"),
    inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events;
    @Column(name = "pinned")
    private Boolean pinned;
    @Column(name = "title")
    private String title;
}
