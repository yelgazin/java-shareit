package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingConverter;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.BookingUpdateRequest;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingConverter bookingConverter;
    private final BookingService bookingService;

    @PostMapping
    public BookingResponse create(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @RequestBody BookingCreateRequest bookingCreateRequest) {
        return bookingConverter.convert(
                bookingService.create(userId, bookingConverter.convertCreateRequestDto(bookingCreateRequest)));
    }

    @PatchMapping("/{bookingId}")
    public BookingResponse update(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long bookingId,
                                  @RequestBody BookingUpdateRequest bookingUpdateRequest) {
        return bookingConverter.convert(
                bookingService.update(userId,
                        bookingId,
                        bookingConverter.convertUpdateRequestDto(bookingUpdateRequest)));
    }

    @PatchMapping("/{bookingId}/")
    public BookingResponse approve(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable long bookingId,
                                   @RequestParam boolean approved) {
        return bookingConverter.convert(bookingService.setApproved(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingResponse getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable long bookingId) {
        return bookingConverter.convert(bookingService.getById(userId, bookingId));
    }

    @GetMapping
    public List<BookingResponse> getCurrentUserBookingsByState(@RequestHeader("X-Sharer-User-Id") long userId,
                                                               @RequestParam(value = "state", defaultValue = "ALL") StateFilter stateFilter) {
        return bookingConverter.convert(bookingService.findByStateFilter(userId, stateFilter));
    }

    @GetMapping("/owner")
    public List<BookingResponse> getOwnerBookingsByState(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @RequestParam(value = "state", defaultValue = "ALL") StateFilter stateFilter) {
        return bookingConverter.convert(bookingService.findByStateFilter(userId, stateFilter));
    }
}
