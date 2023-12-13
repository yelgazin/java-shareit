package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemRequestConverter {

    ItemRequestDto convert(ItemRequest itemRequest);

    List<ItemRequestDto> convert(List<ItemRequest> itemRequests);

    ItemRequest convert(ItemRequestDto itemRequestDto);
}
