package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingRepositoryTest {

    private static final LocalDateTime FIRST_START_DATE = LocalDateTime.of(2021, 1, 1, 0, 0);
    private static final LocalDateTime FIRST_END_DATE = LocalDateTime.of(2021, 2, 1, 0, 0);
    private static final LocalDateTime SECOND_START_DATE = LocalDateTime.of(2022, 1, 1, 0, 0);
    private static final LocalDateTime SECOND_END_DATE = LocalDateTime.of(2022, 2, 1, 0, 0);
    private static final LocalDateTime THIRD_START_DATE = LocalDateTime.of(2023, 1, 1, 0, 0);
    private static final LocalDateTime THIRD_END_DATE = LocalDateTime.of(2023, 2, 1, 0, 0);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private long bookerId;
    private long ownerId;

    @BeforeEach
    void setUp() {
        User owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@mail.ru");
        owner = userRepository.save(owner);
        ownerId = owner.getId();

        User booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@mail.ru");
        booker = userRepository.save(booker);
        bookerId = booker.getId();

        Item item = new Item();
        item.setName("Item");
        item.setDescription("Description");
        item.setOwner(owner);
        item.setAvailable(true);
        item = itemRepository.save(item);

        Booking booking1 = new Booking();
        booking1.setBooker(booker);
        booking1.setItem(item);
        booking1.setStart(FIRST_START_DATE);
        booking1.setEnd(FIRST_END_DATE);
        booking1.setStatus(Status.WAITING);
        bookingRepository.save(booking1);

        Booking booking2 = new Booking();
        booking2.setBooker(booker);
        booking2.setItem(item);
        booking2.setStart(SECOND_START_DATE);
        booking2.setEnd(SECOND_END_DATE);
        booking2.setStatus(Status.APPROVED);
        bookingRepository.save(booking2);

        Booking booking3 = new Booking();
        booking3.setBooker(booker);
        booking3.setItem(item);
        booking3.setStart(THIRD_START_DATE);
        booking3.setEnd(THIRD_END_DATE);
        booking3.setStatus(Status.REJECTED);
        bookingRepository.save(booking3);
    }

    @Test
    void findByBookerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(bookerId);
        assertEquals(3, bookings.size());
        assertEquals(THIRD_START_DATE, bookings.get(0).getStart());
        assertEquals(SECOND_START_DATE, bookings.get(1).getStart());
        assertEquals(FIRST_START_DATE, bookings.get(2).getStart());
    }

    @Test
    void findByBookerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc() {

        LocalDateTime searchDateTime = FIRST_START_DATE.plusDays(1);
        List<Booking> bookings =
                bookingRepository.findByBookerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
                        bookerId, searchDateTime, searchDateTime
                );

        assertEquals(1, bookings.size());
    }

    @Test
    void findByBookerIdAndEndLessThanOrderByStartDesc() {
        LocalDateTime searchDateTime = SECOND_END_DATE.plusDays(1);

        List<Booking> bookings = bookingRepository.findByBookerIdAndEndLessThanOrderByStartDesc(bookerId,
                searchDateTime);

        assertEquals(2, bookings.size());
        assertEquals(SECOND_START_DATE, bookings.get(0).getStart());
        assertEquals(FIRST_START_DATE, bookings.get(1).getStart());
    }

    @Test
    void findByBookerIdAndStartGreaterThanOrderByStartDesc() {
        LocalDateTime searchDateTime = FIRST_END_DATE.minusDays(1);

        List<Booking> bookings = bookingRepository.findByBookerIdAndStartGreaterThanOrderByStartDesc(bookerId,
                searchDateTime);

        assertEquals(2, bookings.size());
        assertEquals(THIRD_START_DATE, bookings.get(0).getStart());
        assertEquals(SECOND_START_DATE, bookings.get(1).getStart());
    }

    @Test
    void findByBookerIdAndStartLessThanAndStatusIsOrderByStartDesc() {
        LocalDateTime searchDateTime = THIRD_START_DATE.plusDays(1);
        List<Booking> bookings = bookingRepository.findByBookerIdAndStartLessThanAndStatusIsOrderByStartDesc(bookerId,
                searchDateTime,
                Status.REJECTED);

        assertEquals(1, bookings.size());
    }

    @Test
    void findByBookerIdAndStatusIsOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByBookerIdAndStatusIsOrderByStartDesc(bookerId,
                Status.REJECTED);

        assertEquals(1, bookings.size());
    }

    @Test
    void findByItemOwnerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId);
        assertEquals(3, bookings.size());
        assertEquals(THIRD_START_DATE, bookings.get(0).getStart());
        assertEquals(SECOND_START_DATE, bookings.get(1).getStart());
        assertEquals(FIRST_START_DATE, bookings.get(2).getStart());
    }

    @Test
    void findByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc() {
        LocalDateTime searchDateTime = FIRST_START_DATE.plusDays(1);
        List<Booking> bookings =
                bookingRepository.findByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
                        ownerId, searchDateTime, searchDateTime
                );

        assertEquals(1, bookings.size());
    }

    @Test
    void findByItemOwnerIdAndEndLessThanOrderByStartDesc() {
        LocalDateTime searchDateTime = SECOND_END_DATE.plusDays(1);

        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndEndLessThanOrderByStartDesc(
                ownerId,
                searchDateTime);

        assertEquals(2, bookings.size());
        assertEquals(SECOND_START_DATE, bookings.get(0).getStart());
        assertEquals(FIRST_START_DATE, bookings.get(1).getStart());
    }

    @Test
    void findByItemOwnerIdAndStartGreaterThanOrderByStartDesc() {
        LocalDateTime searchDateTime = FIRST_END_DATE.minusDays(1);

        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndStartGreaterThanOrderByStartDesc(
                ownerId,
                searchDateTime);

        assertEquals(2, bookings.size());
        assertEquals(THIRD_START_DATE, bookings.get(0).getStart());
        assertEquals(SECOND_START_DATE, bookings.get(1).getStart());
    }

    @Test
    void findByItemOwnerIdAndStatusIsOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndStatusIsOrderByStartDesc(ownerId,
                Status.REJECTED);

        assertEquals(1, bookings.size());
    }

    @Test
    void findByItemId() {
        assertEquals(3, bookingRepository.findByItemId(1).size());
    }
}