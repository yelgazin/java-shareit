package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JoinFormula;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.common.AbstractEntity;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.util.Set;

/**
 * Вещь.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Item extends AbstractEntity {

    @ManyToOne
    User owner;

    String name;
    String description;
    Boolean available;

    @OneToMany
    Set<Comment> comments;

    @ManyToOne
    @JoinFormula("(SELECT b.id FROM booking b " +
            "WHERE b.item_id = id " +
            "AND b.start_time > CURRENT_TIMESTAMP() " +
            "AND b.status = 'APPROVED' " +
            "ORDER BY b.start_time ASC LIMIT 1)")
    private Booking nextBooking;

    @ManyToOne
    @JoinFormula("(SELECT b.id FROM booking b " +
            "WHERE b.item_id = id " +
            "AND b.start_time < CURRENT_TIMESTAMP() " +
            "AND b.status = 'APPROVED' " +
            "ORDER BY b.start_time DESC LIMIT 1)")
    private Booking lastBooking;
}
