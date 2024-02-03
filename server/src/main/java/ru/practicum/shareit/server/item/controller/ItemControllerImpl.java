package ru.practicum.shareit.server.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.server.item.entity.Item;
import ru.practicum.shareit.server.item.service.ItemService;
import ru.practicum.shareit.server.item.dto.*;
import ru.practicum.shareit.server.item.mapper.CommentConverter;
import ru.practicum.shareit.server.item.mapper.ItemConverter;

import java.util.List;

/**
 * Имплементация контроллера для {@link Item}.
 */
@RestController
@RequiredArgsConstructor
public class ItemControllerImpl implements ItemController {

    private final ItemConverter itemConverter;
    private final CommentConverter commentConverter;
    private final ItemService itemService;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ItemResponse> getByUserId(long userId, long from, int size) {
        return itemConverter.convert(itemService.getByUserId(userId, from, size), userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemResponse getById(long userId, long id) {
        return itemConverter.convert(itemService.getById(userId, id), userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemResponse create(long userId, ItemCreateRequest itemCreateRequest) {
        return itemConverter.convert(
                itemService.create(userId, itemConverter.convertCreateRequestDto(itemCreateRequest)),
                userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemResponse update(long userId, long id, ItemUpdateRequest itemUpdateRequest) {
        return itemConverter.convert(
                itemService.update(userId, id, itemConverter.convertUpdateRequestDto(itemUpdateRequest)),
                userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ItemResponse> search(long userId, String text, long from, int size) {
        return itemConverter.convert(itemService.findAvailableBySubstring(text, from, size), userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommentResponse addComment(long userId, long itemId, CommentCreateRequest commentCreateRequest) {
        return commentConverter.convert(
                itemService.addComment(userId, itemId, commentConverter.convert(commentCreateRequest)));
    }
}