package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "endpoint_hits")
@Getter
@Setter
public class EndpointHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "app", length = 255)
    private String app;

    @Column(name = "uri", length = 255)
    private String uri;

    @Column(name = "ip", length = 15)
    private String ip;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
