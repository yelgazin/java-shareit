package ru.practicum.shareit.request;

import java.util.List;

public interface ItemRequestService {
    ItemRequest create(long userId, ItemRequest itemRequest);

    List<ItemRequest> getAllMyRequests(long userId);

    List<ItemRequest> getAllNotUserRequests(long userId, long from, int size);

    ItemRequest getByRequestId(long userId, long requestId);
}
