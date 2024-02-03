package ru.practicum.shareit.server.booking;

import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.booking.entity.Booking;
import ru.practicum.shareit.server.booking.entity.StateFilter;
import ru.practicum.shareit.server.booking.entity.Status;
import ru.practicum.shareit.server.booking.service.BookingServiceImpl;
import ru.practicum.shareit.server.common.exception.ForbiddenException;
import ru.practicum.shareit.server.helper.ObjectGenerator;
import ru.practicum.shareit.server.item.entity.Item;
import ru.practicum.shareit.server.item.service.ItemService;
import ru.practicum.shareit.server.user.entity.User;
import ru.practicum.shareit.server.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase
@SpringBootTest
@Import(ObjectGenerator.class)
@Transactional
@Rollback
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplIT {

    private final UserService userService;
    private final ItemService itemService;
    private final BookingServiceImpl bookingService;
    private final ObjectGenerator objectGenerator;

    private Item item;
    private User owner;
    private User booker;
    private User anotherUser;
    private Booking booking;

    private LocalDateTime currentDateTime;


    @BeforeEach
    void setUp() {
        currentDateTime = LocalDateTime.now();

        owner = objectGenerator.next(User.class);
        booker = objectGenerator.next(User.class);
        anotherUser = objectGenerator.next(User.class);
        item = objectGenerator.next(Item.class);
        booking = objectGenerator.next(Booking.class);

        item.setOwner(owner);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(currentDateTime.plusDays(1));
        booking.setEnd(currentDateTime.plusDays(3));
        booking.setStatus(Status.WAITING);

        owner = userService.create(owner);
        booker = userService.create(booker);
        anotherUser = userService.create(anotherUser);
        item = itemService.create(owner.getId(), item);
        booking = bookingService.create(booker.getId(), booking);
    }

    @Test
    void getById_whenBookingExistsAndRequestOwner_thenBookingReturned() {
        long bookingId = booking.getId();
        long userId = owner.getId();

        Booking foundBooking = bookingService.getById(userId, bookingId);

        MatcherAssert.assertThat(foundBooking, Matchers.equalTo(booking));
    }

    @Test
    void getById_whenBookingExistsAndRequestBooker_thenBookingReturned() {
        long bookingId = booking.getId();
        long userId = booker.getId();

        Booking foundBooking = bookingService.getById(userId, bookingId);

        MatcherAssert.assertThat(foundBooking, Matchers.equalTo(booking));
    }

    @Test
    void getById_whenBookingExistsAndRequestNeitherOwnerNorBooker_thenBookingReturned() {
        long bookingId = booking.getId();
        long userId = anotherUser.getId();

        assertThrows(ForbiddenException.class, () -> bookingService.getById(userId, bookingId));
    }

    @Test
    void findByBookerId_whenBookingExists_thenListOfBookingsReturned() {
        long bookerId = booker.getId();
        StateFilter stateFilter = StateFilter.ALL;
        long from = 0;
        int size = 50;

        List<Booking> foundBookings = bookingService.findByBookerId(bookerId, stateFilter, from, size);

        MatcherAssert.assertThat(foundBookings, Matchers.contains(booking));
    }

    @Test
    void findByItemOwner_whenBookingExists_thenListOfBookingsReturned() {
        long itemOwnerId = item.getOwner().getId();
        StateFilter stateFilter = StateFilter.ALL;
        long from = 0;
        int size = 50;

        List<Booking> foundBookings = bookingService.findByItemOwner(itemOwnerId, stateFilter, from, size);

        MatcherAssert.assertThat(foundBookings, Matchers.contains(booking));
    }

    @Test
    void setApproved_whenBookingExists_thenApprovedBookingReturned() {
        long ownerId = item.getOwner().getId();
        long bookingId = booking.getId();
        boolean approved = true;

        bookingService.setApproved(ownerId, bookingId, approved);
        Booking foundBooking = bookingService.getById(ownerId, bookingId);

        MatcherAssert.assertThat(foundBooking, Matchers.is(booking));
        MatcherAssert.assertThat(foundBooking.getStatus(), Matchers.is(Status.APPROVED));
    }
}