package ru.practicum.shareit.server.booking.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.common.entity.AbstractEntity;
import ru.practicum.shareit.server.item.entity.Item;
import ru.practicum.shareit.server.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Бронирование.
 */
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Booking extends AbstractEntity {

    @ToString.Exclude
    @ManyToOne
    User booker;

    @ToString.Exclude
    @ManyToOne
    Item item;

    @Enumerated(EnumType.STRING)
    Status status;
    @Column(name = "start_time")
    LocalDateTime start;
    @Column(name = "end_time")
    LocalDateTime end;
}
