package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.AbstractEntity;

/**
 * Пользователь.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractEntity {
    String name;
    String email;
}
