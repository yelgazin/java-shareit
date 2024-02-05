package ru.practicum.shareit.server.item.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.booking.entity.Booking;
import ru.practicum.shareit.server.common.entity.AbstractEntity;
import ru.practicum.shareit.server.user.entity.User;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.Set;

/**
 * Вещь.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Entity
public class Item extends AbstractEntity {

    @ToString.Exclude
    @ManyToOne
    User owner;

    String name;
    String description;
    Boolean available;

    Long requestId;

    @ToString.Exclude
    @OneToMany(mappedBy = "item")
    Set<Comment> comments;

    @Transient
    Booking nextBooking;

    @Transient
    Booking lastBooking;
}
