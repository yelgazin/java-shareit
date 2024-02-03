package ru.practicum.shareit.gateway.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.gateway.request.client.ItemRequestClient;
import ru.practicum.shareit.server.request.entity.ItemRequest;
import ru.practicum.shareit.server.request.dto.ItemRequestCreateRequest;

/**
 * Имплементация контроллера для {@link ItemRequest}
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemRequestControllerImpl implements ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> create(long userId, ItemRequestCreateRequest itemRequestCreateRequest) {
        log.debug("Создание запроса. ИД пользователя: {}.", userId);
        return itemRequestClient.create(userId, itemRequestCreateRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> getAllMyRequests(long userId) {
        log.debug("Получение списка запросов пользователя. ИД пользователя: {}.", userId);
        return itemRequestClient.getAllMyRequests(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> getAllNotMyRequests(long userId, long from, int size) {
        log.debug("Получение всех запросов других пользователей. ИД пользователя: {}. Записи с {} по {}.",
                userId, from, size);
        return itemRequestClient.getAllNotMyRequests(userId, from, size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> getByRequestId(long userId, long requestId) {
        log.debug("Получение запроса по индентификатору. ИД пользователя: {}. ИД запроса: {}", userId, requestId);
        return itemRequestClient.getByRequestId(userId, requestId);
    }
}
