package ru.practicum.shareit.item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.AbstractController;
import ru.practicum.shareit.item.dto.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Контроллер для {@link Item}.
 */
@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Tag(name = "Items", description = "Управление вещами")
public class ItemController extends AbstractController {

    private final ItemConverter itemConverter;
    private final CommentConverter commentConverter;
    private final ItemService itemService;

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
    public List<ItemResponse> getByUserId(@RequestHeader(USER_ID_HEADER) long userId,
                                          @RequestParam(defaultValue = "0") @PositiveOrZero long from,
                                          @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) @Positive int size) {
        return itemConverter.convert(itemService.getByUserId(userId, from, size), userId);
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
    public ItemResponse getById(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable long id) {
        return itemConverter.convert(itemService.getById(id), userId);
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
    public ItemResponse create(@RequestHeader(USER_ID_HEADER) long userId,
                               @Valid @RequestBody ItemCreateRequest itemCreateRequest) {
        return itemConverter.convert(
                itemService.create(userId, itemConverter.convertCreateRequestDto(itemCreateRequest)), userId);
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
    public ItemResponse update(@RequestHeader(USER_ID_HEADER) long userId,
                               @PathVariable long id,
                               @Valid @RequestBody ItemUpdateRequest itemUpdateRequest) {
        return itemConverter.convert(
                itemService.update(userId, id, itemConverter.convertUpdateRequestDto(itemUpdateRequest)), userId);
    }

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
    public List<ItemResponse> search(@RequestHeader(USER_ID_HEADER) long userId,
                                     @RequestParam String text,
                                     @RequestParam(defaultValue = "0") @PositiveOrZero long from,
                                     @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) @Positive int size) {
        return itemConverter.convert(itemService.findAvailableBySubstring(text, from, size), userId);
    }

    /**
     * Добавление комментария к вещи.
     * @param userId идентификатор пользователя.
     * @param itemId идентификатор вещи.
     * @param commentCreateRequest параметры для комментария.
     * @return созданный комментарий.
     */
    @PostMapping("/{itemId}/comment")
    @Operation(summary = "Добавление комментария к вещи")
    public CommentResponse addComment(@RequestHeader(USER_ID_HEADER) long userId,
                                      @PathVariable long itemId,
                                      @Valid @RequestBody CommentCreateRequest commentCreateRequest) {
        return commentConverter.convert(
                itemService.addComment(userId, itemId, commentConverter.convert(commentCreateRequest)));
    }
}