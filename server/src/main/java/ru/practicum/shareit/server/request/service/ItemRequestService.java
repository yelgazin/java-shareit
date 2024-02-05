package ru.practicum.shareit.server.request.service;

import ru.practicum.shareit.server.request.entity.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest create(long userId, ItemRequest itemRequest);

    List<ItemRequest> getAllMyRequests(long userId);

    List<ItemRequest> getAllNotUserRequests(long userId, long from, int size);

    ItemRequest getByRequestId(long userId, long requestId);
}
