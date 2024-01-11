package ru.practicum.shareit.booking;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingConverter;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер для {@link Booking}.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Tag(name = "Bookings", description = "Управление бронированиями")
public class BookingController {

    private final BookingConverter bookingConverter;
    private final BookingService bookingService;

    /**
     * Создание бронирования.
     *
     * @param userId               идентификатор пользователя.
     * @param bookingCreateRequest параметр для создания бронирования.
     * @return список бронирований.
     */
    @PostMapping
    @Operation(summary = "Создание бронирования")
    public BookingResponse create(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @Valid @RequestBody BookingCreateRequest bookingCreateRequest) {
        return bookingConverter.convert(
                bookingService.create(userId, bookingConverter.convertCreateRequestDto(bookingCreateRequest)));
    }

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
    public BookingResponse approve(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable long bookingId,
                                   @RequestParam boolean approved) {
        return bookingConverter.convert(bookingService.setApproved(userId, bookingId, approved));
    }

    /**
     * Получение бронирования по идентификатору.
     *
     * @param userId    идентификатор пользователя.
     * @param bookingId идентификатор бронирования.
     * @return найденное бронирование.
     */
    @GetMapping("/{bookingId}")
    @Operation(summary = "Получение бронирования по идентификатору")
    public BookingResponse getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable long bookingId) {
        return bookingConverter.convert(bookingService.getById(userId, bookingId));
    }

    /**
     * Получение всех бронирований пользователя.
     *
     * @param userId      идентификатор пользователя.
     * @param stateFilter фильтр отбора.
     * @return найденные бронирования.
     */
    @GetMapping
    @Operation(summary = "Получение всех бронирований пользователя")
    public List<BookingResponse> getByBookerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(value = "state", defaultValue = "ALL") String stateFilter) {
        return bookingConverter.convert(bookingService.findByBookerId(userId, StateFilter.parse(stateFilter)));
    }

    /**
     * Получение всех бронирований вещей владельца.
     *
     * @param userId      идентификатор пользователя.
     * @param stateFilter фильтр отбора.
     * @return найденные бронирования.
     */
    @GetMapping("/owner")
    @Operation(summary = "Получение всех бронирований вещей владельца")
    public List<BookingResponse> getByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @Parameter(name = "фильтр отбора",
                                                    description = "Возможные значения " +
                                                            "ALL, CURRENT, PAST, FUTURE, " +
                                                            "WAITING, APPROVED, REJECTED")
                                            @RequestParam(value = "state", defaultValue = "ALL") String stateFilter) {
        return bookingConverter.convert(bookingService.findByItemOwner(userId, StateFilter.parse(stateFilter)));
    }
}
