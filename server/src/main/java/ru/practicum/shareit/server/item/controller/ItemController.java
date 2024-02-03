package ru.practicum.shareit.server.item.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.common.controller.Controller;
import ru.practicum.shareit.server.item.entity.Item;
import ru.practicum.shareit.server.item.dto.*;

import java.util.List;

/**
 * Контроллер для {@link Item}.
 */
@RequestMapping("/items")
@Tag(name = "Items", description = "Управление вещами")
public interface ItemController extends Controller {

    /**
     * Получение списка вещей пользователя (владельца).
     *
     * @param userId идентификатор пользователя.
     * @param from   индекс первого элемента.
     * @param size   количество элементов для отображения.
     * @return список вещей пользователя.
     */
    @GetMapping
    @Operation(summary = "Получение списка вещей пользователя")
    List<ItemResponse> getByUserId(@RequestHeader(USER_ID_HEADER) long userId,
                                   @RequestParam(defaultValue = "0") long from,
                                   @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size);

    /**
     * Получение вещи по идентификатору.
     *
     * @param userId идентификатор пользователя (не владельца).
     * @param id     идентификатор вещи.
     * @return вещь.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение вещи по идентификатору")
    ItemResponse getById(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable long id);

    /**
     * Создание вещи.
     *
     * @param userId            идентификатор пользователя.
     * @param itemCreateRequest параметры вещи.
     * @return созданная вещь.
     */
    @PostMapping
    @Operation(summary = "Публикация вещи")
    ItemResponse create(@RequestHeader(USER_ID_HEADER) long userId,
                        @RequestBody ItemCreateRequest itemCreateRequest);

    /**
     * Обновление вещи.
     *
     * @param userId            идентификатор пользователя.
     * @param id                идентификатор вещи.
     * @param itemUpdateRequest параметры для обновления.
     * @return обновленная вещь.
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Обновление вещи")
    ItemResponse update(@RequestHeader(USER_ID_HEADER) long userId,
                        @PathVariable long id,
                        @RequestBody ItemUpdateRequest itemUpdateRequest);

    /**
     * Поиск доступных вещей по наименованию или описанию.
     *
     * @param userId идентификатор пользователя.
     * @param text   подстрока для регистронезависимого поиска.
     * @param from   индекс первого элемента.
     * @param size   количество элементов для отображения.
     * @return список найденных доступных вещей.
     */
    @GetMapping("/search")
    @Operation(summary = "Поиск доступных вещей по наименованию или описанию")
    List<ItemResponse> search(@RequestHeader(USER_ID_HEADER) long userId,
                              @RequestParam String text,
                              @RequestParam(defaultValue = "0") long from,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size);

    /**
     * Добавление комментария к вещи.
     *
     * @param userId               идентификатор пользователя.
     * @param itemId               идентификатор вещи.
     * @param commentCreateRequest параметры для комментария.
     * @return созданный комментарий.
     */
    @PostMapping("/{itemId}/comment")
    @Operation(summary = "Добавление комментария к вещи")
    CommentResponse addComment(@RequestHeader(USER_ID_HEADER) long userId,
                               @PathVariable long itemId,
                               @RequestBody CommentCreateRequest commentCreateRequest);
}