package ru.practicum.shareit.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id"})
public abstract class AbstractEntity {
    private Long id;
}
