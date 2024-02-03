package ru.practicum.shareit.server.item.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.server.common.entity.AbstractEntity;
import ru.practicum.shareit.server.user.entity.User;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * Комментарий.
 */
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Comment extends AbstractEntity {

    @ToString.Exclude
    @ManyToOne
    User author;

    @CreationTimestamp
    LocalDateTime created;

    String text;

    @ToString.Exclude
    @ManyToOne
    Item item;
}
