package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.AbstractEntity;

/**
 * TODO Sprint add-controllers.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class Item extends AbstractEntity {
    Long id;
    Long ownerId;
    String name;
    String description;
    Boolean available;
}
