package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import ru.practicum.shareit.common.AbstractEntityCopier;

@Mapper(componentModel = "spring")
public interface BookingCopier extends AbstractEntityCopier<Booking> {
}
