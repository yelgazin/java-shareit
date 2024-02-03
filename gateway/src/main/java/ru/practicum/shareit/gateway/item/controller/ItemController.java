package ru.practicum.shareit.gateway.item.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.common.controller.Controller;
import ru.practicum.shareit.server.item.entity.Item;
import ru.practicum.shareit.server.item.dto.CommentCreateRequest;
import ru.practicum.shareit.server.item.dto.ItemCreateRequest;
import ru.practicum.shareit.server.item.dto.ItemUpdateRequest;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Контроллер для {@link Item}.
 */
@Validated
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
    ResponseEntity<Object> getByUserId(@RequestHeader(USER_ID_HEADER) long userId,
                                       @RequestParam(defaultValue = "0") @PositiveOrZero long from,
                                       @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) @Positive int size);

    /**
     * Получение вещи по идентификатору.
     *
     * @param userId идентификатор пользователя (не владельца).
     * @param id     идентификатор вещи.
     * @return вещь.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение вещи по идентификатору")
    ResponseEntity<Object> getById(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable long id);

    /**
     * Создание вещи.
     *
     * @param userId            идентификатор пользователя.
     * @param itemCreateRequest параметры вещи.
     * @return созданная вещь.
     */
    @PostMapping
    @Operation(summary = "Публикация вещи")
    ResponseEntity<Object> create(@RequestHeader(USER_ID_HEADER) long userId,
                                  @Validated @RequestBody ItemCreateRequest itemCreateRequest);

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
    ResponseEntity<Object> update(@RequestHeader(USER_ID_HEADER) long userId,
                                  @PathVariable long id,
                                  @Validated @RequestBody ItemUpdateRequest itemUpdateRequest);

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
    ResponseEntity<Object> search(@RequestHeader(USER_ID_HEADER) long userId,
                                  @RequestParam String text,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero long from,
                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) @Positive int size);

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
    ResponseEntity<Object> addComment(@RequestHeader(USER_ID_HEADER) long userId,
                                      @PathVariable long itemId,
                                      @Validated @RequestBody CommentCreateRequest commentCreateRequest);
}