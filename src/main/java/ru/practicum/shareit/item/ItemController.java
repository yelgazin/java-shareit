package ru.practicum.shareit.item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер для {@link Item}.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Tag(name = "Items", description = "Управление вещами")
public class ItemController {

    private final ItemConverter itemConverter;
    private final CommentConverter commentConverter;
    private final ItemService itemService;

    /**
     * Получение списка вещей пользователя (владельца).
     *
     * @param userId идентификатор пользователя.
     * @return список вещей пользователя.
     */
    @GetMapping
    @Operation(summary = "Получение списка вещей пользователя")
    public List<ItemResponse> getByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemConverter.convert(itemService.getByUserId(userId));
    }

    /**
     * Получение вещи по идентификатору.
     *
     * @param userId идентификатор пользователя (не владельца).
     * @param id     идентификатор вещи.
     * @return вещь.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение вещи по идентификатору")
    public ItemResponse getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        return itemConverter.convert(itemService.getById(id));
    }

    /**
     * Создание вещи.
     *
     * @param userId            идентификатор пользователя.
     * @param itemCreateRequest параметры вещи.
     * @return созданная вещь.
     */
    @PostMapping
    @Operation(summary = "Публикация вещи")
    public ItemResponse create(@RequestHeader("X-Sharer-User-Id") long userId,
                               @Valid @RequestBody ItemCreateRequest itemCreateRequest) {
        return itemConverter.convert(
                itemService.create(userId, itemConverter.convertCreateRequestDto(itemCreateRequest)));
    }

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
    public ItemResponse update(@RequestHeader("X-Sharer-User-Id") long userId,
                               @PathVariable long id,
                               @Valid @RequestBody ItemUpdateRequest itemUpdateRequest) {
        return itemConverter.convert(
                itemService.update(userId, id, itemConverter.convertUpdateRequestDto(itemUpdateRequest)));
    }

    /**
     * Поиск доступных вещей по наименованию или описанию.
     *
     * @param userId идентификатор пользователя.
     * @param text   подстрока для регистронезависимого поиска.
     * @return список найденных доступных вещей.
     */
    @GetMapping("/search")
    @Operation(summary = "Поиск доступных вещей по наименованию или описанию")
    public List<ItemResponse> search(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @RequestParam String text) {
        return itemConverter.convert(itemService.findAvailableBySubstring(text));
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponse addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable long itemId,
                                      @Valid @RequestBody CommentCreateRequest commentCreateRequest) {
        return commentConverter.convert(
                itemService.addComment(userId, itemId, commentConverter.convertCreateRequestDto(commentCreateRequest)));
    }
}