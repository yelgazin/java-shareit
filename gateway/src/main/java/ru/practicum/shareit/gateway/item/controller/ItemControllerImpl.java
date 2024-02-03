package ru.practicum.shareit.gateway.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.gateway.item.client.ItemClient;
import ru.practicum.shareit.server.item.entity.Item;
import ru.practicum.shareit.server.item.dto.CommentCreateRequest;
import ru.practicum.shareit.server.item.dto.ItemCreateRequest;
import ru.practicum.shareit.server.item.dto.ItemUpdateRequest;

/**
 * Имплементация контроллера для {@link Item}.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemControllerImpl implements ItemController {

    private final ItemClient itemClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> getByUserId(long userId, long from, int size) {
        log.debug("Получение вещей пользователя. ИД пользователя: {}. Записи с {} по {}.",
                userId, from, size);
        return itemClient.getByUserId(userId, from, size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> getById(long userId, long id) {
        log.debug("Получение вещи по идентификатору. ИД пользователя: {}. ИД вещий: {}", userId, id);
        return itemClient.getById(userId, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> create(long userId, ItemCreateRequest itemCreateRequest) {
        log.debug("Создание вещи. ИД пользователя: {}.", userId);
        return itemClient.create(userId, itemCreateRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> update(long userId, long id, ItemUpdateRequest itemUpdateRequest) {
        log.debug("Обновление вещи. ИД пользователя: {}. ИД обновляемой вещи: {}.", userId, id);
        return itemClient.update(userId, id, itemUpdateRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> search(long userId, String text, long from, int size) {
        log.debug("Поиск доступных вещей по наименованию или описанию. ИД пользователя: {}. " +
                "Строка для поиска: \"{}\".Записи с {} по {}.", text, userId, from, size);
        return itemClient.search(userId, text, from, size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> addComment(long userId, long itemId, CommentCreateRequest commentCreateRequest) {
        log.debug("Добавление комментария к вещи. ИД пользователя: {}. ИД вещи: {}", userId, itemId);
        return itemClient.addComment(userId, itemId, commentCreateRequest);
    }
}