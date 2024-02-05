package ru.practicum.shareit.server.request.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.request.dto.ItemRequestCreateRequest;
import ru.practicum.shareit.server.request.dto.ItemRequestResponse;
import ru.practicum.shareit.server.request.entity.ItemRequest;

import java.util.List;

import static ru.practicum.shareit.server.config.RequestConstants.DEFAULT_PAGE_SIZE;
import static ru.practicum.shareit.server.config.RequestConstants.USER_ID_HEADER;

/**
 * Контроллер для {@link ItemRequest}
 */
@RequestMapping(path = "/requests")
@Tag(name = "Requests", description = "Управление запросами на вещи")
public interface ItemRequestController {

    /**
     * Создание запроса на вещь.
     *
     * @param userId                   идентификатор пользователя.
     * @param itemRequestCreateRequest параметры запроса вещи.
     * @return созданный запрос.
     */
    @PostMapping
    @Operation(summary = "Публикация запроса на вещь")
    ItemRequestResponse create(@RequestHeader(USER_ID_HEADER) long userId,
                               @RequestBody ItemRequestCreateRequest itemRequestCreateRequest);

    /**
     * Получение всех запросов вещей пользователя (владельца запроса)
     *
     * @param userId идентификатор пользователя.
     * @return список запросов вещей.
     */
    @Operation(description = "Получение списка запросов пользователя")
    @GetMapping
    List<ItemRequestResponse> getAllMyRequests(@RequestHeader(USER_ID_HEADER) long userId);

    /**
     * Получение всех запросов других пользователей
     *
     * @param userId идентификатор пользователя.
     * @param from   индекс первого элемента.
     * @param size   количество элементов для отображения.
     * @return список запросов вещей.
     */
    @Operation(description = "Получение всех запросов других пользователей")
    @GetMapping("/all")
    List<ItemRequestResponse> getAllNotMyRequests(@RequestHeader(USER_ID_HEADER) long userId,
                                                  @RequestParam(defaultValue = "0") long from,
                                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size);

    /**
     * Получение запроса по индентификатору.
     *
     * @param userId    идентификатор пользователя.
     * @param requestId идентификатор запроса.
     * @return запрос вещи.
     */
    @GetMapping("/{requestId}")
    @Operation(description = "Получение запроса по индентификатору")
    ItemRequestResponse getByRequestId(@RequestHeader(USER_ID_HEADER) long userId,
                                       @PathVariable long requestId);
}
