package ru.practicum.shareit.server.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.server.request.entity.ItemRequest;
import ru.practicum.shareit.server.request.service.ItemRequestService;
import ru.practicum.shareit.server.request.mapper.ItemRequestConverter;
import ru.practicum.shareit.server.request.dto.ItemRequestCreateRequest;
import ru.practicum.shareit.server.request.dto.ItemRequestResponse;

import java.util.List;

/**
 * Имплементация контроллера для {@link ItemRequest}
 */
@RestController
@RequiredArgsConstructor
public class ItemRequestControllerImpl implements ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final ItemRequestConverter itemRequestConverter;

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemRequestResponse create(long userId, ItemRequestCreateRequest itemRequestCreateRequest) {
        return itemRequestConverter.convert(
                itemRequestService.create(userId, itemRequestConverter.convert(itemRequestCreateRequest))
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ItemRequestResponse> getAllMyRequests(long userId) {
        return itemRequestConverter.convert(itemRequestService.getAllMyRequests(userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ItemRequestResponse> getAllNotMyRequests(long userId, long from, int size) {
        return itemRequestConverter.convert(itemRequestService.getAllNotUserRequests(userId, from, size));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemRequestResponse getByRequestId(long userId, long requestId) {
        return itemRequestConverter.convert(itemRequestService.getByRequestId(userId, requestId));
    }
}
