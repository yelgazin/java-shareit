package ru.practicum.shareit.server.request.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.server.common.entity.AbstractEntity;
import ru.practicum.shareit.server.item.entity.Item;
import ru.practicum.shareit.server.user.entity.User;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Запрос вещи.
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "request")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequest extends AbstractEntity {

    @ToString.Exclude
    @ManyToOne
    User author;

    String description;

    @CreationTimestamp
    LocalDateTime created;

    @ToString.Exclude
    @OneToMany(mappedBy = "requestId")
    Set<Item> items;
}
