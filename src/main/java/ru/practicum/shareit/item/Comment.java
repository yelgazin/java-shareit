package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.common.AbstractEntity;
import ru.practicum.shareit.user.User;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * Комментарий.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Comment extends AbstractEntity {

    @ManyToOne
    User author;

    @CreationTimestamp
    LocalDateTime created;

    String text;

//    @ManyToOne
//    Item item;
}
