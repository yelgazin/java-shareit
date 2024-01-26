package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

/**
 * Конвертер для {@link ItemResponse}
 */
@Mapper(componentModel = "spring")
public interface ItemRequestConverter {

    ItemRequestResponse convert(ItemRequest itemRequest);

    List<ItemRequestResponse> convert(List<ItemRequest> itemRequests);

    ItemRequest convert(ItemRequestCreateRequest itemRequestCreateRequest);

    ItemRequestResponse.ItemView convert(Item item);
}
