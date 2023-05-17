package ru.yandex.explore.location;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(schema = "explore", name = "location")
@Getter
@Setter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "lat")
    private double lat;
    @Column(name = "lon")
    private double lon;
}
