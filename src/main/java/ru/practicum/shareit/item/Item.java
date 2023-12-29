package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JoinFormula;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.common.AbstractEntity;
import ru.practicum.shareit.user.User;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    @OneToMany(mappedBy = "item")
    private Set<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinFormula("(SELECT b.id FROM booking b " +
            "WHERE b.item_id = id " +
            "AND b.start_time > LOCALTIMESTAMP(6) " +
            "AND b.status = 'APPROVED' " +
            "ORDER BY b.start_time ASC LIMIT 1)")
    private Booking nextBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinFormula("(SELECT b.id FROM booking b " +
            "WHERE b.item_id = id " +
            "AND b.start_time < LOCALTIMESTAMP(6) " +
            "AND b.status = 'APPROVED' " +
            "ORDER BY b.start_time DESC LIMIT 1)")
    private Booking lastBooking;
}