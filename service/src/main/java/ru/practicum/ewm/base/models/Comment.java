package ru.practicum.ewm.base.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "comments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "text", nullable = false)
    String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "event_id", nullable = false)
    Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "author_id", nullable = false)
    User author;

    @Column(name = "created")
    LocalDateTime created;

    @Column(name = "positive", nullable = false)
    Boolean positive;

    @Column(name = "last_modify")
    LocalDateTime lastModify;
}
