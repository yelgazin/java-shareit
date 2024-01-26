package ru.practicum.shareit.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.AbstractEntity;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

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
