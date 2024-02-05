package ru.practicum.shareit.server.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.server.booking.dto.BookingResponse;
import ru.practicum.shareit.server.booking.entity.Booking;
import ru.practicum.shareit.server.booking.entity.StateFilter;

import java.util.List;

import static ru.practicum.shareit.server.config.RequestConstants.DEFAULT_PAGE_SIZE;
import static ru.practicum.shareit.server.config.RequestConstants.USER_ID_HEADER;

/**
 * Контроллер для {@link Booking}.
 */
@RequestMapping(path = "/bookings")
@Tag(name = "Bookings", description = "Управление бронированиями")
public interface BookingController {

    /**
     * Создание бронирования.
     *
     * @param userId               идентификатор пользователя.
     * @param bookingCreateRequest параметр для создания бронирования.
     * @return список бронирований.
     */
    @PostMapping
    @Operation(summary = "Создание бронирования")
    BookingResponse create(@RequestHeader(USER_ID_HEADER) long userId,
                           @RequestBody BookingCreateRequest bookingCreateRequest);

    /**
     * Подтверждение или отклонение бронирования
     *
     * @param userId    идентификатор пользователя.
     * @param bookingId идентификатор бронирования.
     * @param approved  подтверждение или отклонение.
     * @return изменённое бронирование.
     */
    @PatchMapping("/{bookingId}")
    @Operation(summary = "Подтверждение или отклонение бронирования")
    BookingResponse approve(@RequestHeader(USER_ID_HEADER) long userId,
                            @PathVariable long bookingId,
                            @RequestParam boolean approved);

    /**
     * Получение бронирования по идентификатору.
     *
     * @param userId    идентификатор пользователя.
     * @param bookingId идентификатор бронирования.
     * @return найденное бронирование.
     */
    @GetMapping("/{bookingId}")
    @Operation(summary = "Получение бронирования по идентификатору")
    BookingResponse getById(@RequestHeader(USER_ID_HEADER) long userId,
                            @PathVariable long bookingId);

    /**
     * Получение всех бронирований пользователя.
     *
     * @param userId      идентификатор пользователя.
     * @param stateFilter фильтр отбора.
     * @param from        индекс первого элемента.
     * @param size        количество элементов для отображения.
     * @return найденные бронирования.
     */
    @GetMapping
    @Operation(summary = "Получение всех бронирований пользователя")
    List<BookingResponse> getByBookerId(@RequestHeader(USER_ID_HEADER) long userId,
                                        @RequestParam(value = "state", defaultValue = "ALL") StateFilter stateFilter,
                                        @RequestParam(defaultValue = "0") long from,
                                        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size);

    /**
     * Получение всех бронирований вещей владельца.
     *
     * @param userId      идентификатор пользователя.
     * @param stateFilter фильтр отбора.
     * @param from        индекс первого элемента.
     * @param size        количество элементов для отображения.
     * @return найденные бронирования.
     */
    @GetMapping("/owner")
    @Operation(summary = "Получение всех бронирований вещей владельца")
    List<BookingResponse> getByOwner(@RequestHeader(USER_ID_HEADER) long userId,
                                     @Parameter(name = "фильтр отбора",
                                             description = "Возможные значения " +
                                                     "ALL, CURRENT, PAST, FUTURE, " +
                                                     "WAITING, APPROVED, REJECTED")
                                     @RequestParam(value = "state", defaultValue = "ALL") StateFilter stateFilter,
                                     @RequestParam(defaultValue = "0") long from,
                                     @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size);
}
