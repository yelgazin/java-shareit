package ru.practicum.shareit.server.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.server.item.entity.Item;
import ru.practicum.shareit.server.item.dto.ItemResponse;
import ru.practicum.shareit.server.request.dto.ItemRequestCreateRequest;
import ru.practicum.shareit.server.request.dto.ItemRequestResponse;
import ru.practicum.shareit.server.request.entity.ItemRequest;

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
