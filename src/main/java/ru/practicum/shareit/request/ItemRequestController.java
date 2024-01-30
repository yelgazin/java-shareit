package ru.practicum.shareit.request;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.AbstractController;
import ru.practicum.shareit.request.dto.ItemRequestConverter;
import ru.practicum.shareit.request.dto.ItemRequestCreateRequest;
import ru.practicum.shareit.request.dto.ItemRequestResponse;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Контроллер для {@link ItemRequest}
 */
@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Tag(name = "Requests", description = "Управление запросами на вещи")
public class ItemRequestController extends AbstractController {

    private final ItemRequestService itemRequestService;
    private final ItemRequestConverter itemRequestConverter;

    /**
     * Создание запроса на вещь.
     *
     * @param userId                   идентификатор пользователя.
     * @param itemRequestCreateRequest параметры запроса вещи.
     * @return созданный запрос.
     */
    @PostMapping
    @Operation(summary = "Публикация запроса на вещь")
    public ItemRequestResponse create(@RequestHeader(USER_ID_HEADER) long userId,
                                      @Valid @RequestBody ItemRequestCreateRequest itemRequestCreateRequest) {
        return itemRequestConverter.convert(
                itemRequestService.create(userId, itemRequestConverter.convert(itemRequestCreateRequest))
        );
    }

    /**
     * Получение всех запросов вещей пользователя (владельца запроса)
     *
     * @param userId идентификатор пользователя.
     * @return список запросов вещей.
     */
    @Operation(description = "Получение списка запросов пользователя")
    @GetMapping
    public List<ItemRequestResponse> getAllMyRequests(@RequestHeader(USER_ID_HEADER) long userId) {
        return itemRequestConverter.convert(itemRequestService.getAllMyRequests(userId));
    }

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
    public List<ItemRequestResponse> getAllNotMyRequests(@RequestHeader(USER_ID_HEADER) long userId,
                                                         @RequestParam(defaultValue = "0") @PositiveOrZero long from,
                                                         @RequestParam(defaultValue = DEFAULT_PAGE_SIZE)
                                                         @Positive int size) {
        return itemRequestConverter.convert(itemRequestService.getAllNotUserRequests(userId, from, size));
    }

    /**
     * Получение запроса по индентификатору.
     *
     * @param userId    идентификатор пользователя.
     * @param requestId идентификатор запроса.
     * @return запрос вещи.
     */
    @GetMapping("/{requestId}")
    @Operation(description = "Получение запроса по индентификатору")
    public ItemRequestResponse getByRequestId(@RequestHeader(USER_ID_HEADER) long userId,
                                              @PathVariable long requestId) {
        return itemRequestConverter.convert(itemRequestService.getByRequestId(userId, requestId));
    }
}
