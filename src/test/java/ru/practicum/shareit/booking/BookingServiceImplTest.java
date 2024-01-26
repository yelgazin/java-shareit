package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.exception.BookingException;
import ru.practicum.shareit.common.exception.EntityNotFoundException;
import ru.practicum.shareit.common.exception.ForbiddenException;
import ru.practicum.shareit.helper.ObjectGenerator;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private final ObjectGenerator objectGenerator = new ObjectGenerator();

    private Item item;
    private User owner;
    private User booker;
    private Booking booking;

    @BeforeEach
    void setUp() {
        owner = objectGenerator.next(User.class);
        booker = objectGenerator.next(User.class);
        item = objectGenerator.next(Item.class);
        booking = objectGenerator.next(Booking.class);

        owner.setId(1L);
        booker.setId(2L);
        item.setId(1L);
        booking.setId(1L);

        item.setOwner(owner);
        booking.setItem(item);
        booking.setBooker(booker);
    }

    @Test
    void getById_whenInvalidBookingId_thenEntityNotFoundExceptionThrown() {
        long userId = 1L;
        long bookingId = 0L;

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.getById(userId, bookingId));

        verify(bookingRepository).findById(bookingId);
    }

    @Test
    void getById_whenUserOwnerAndNotBooker_thenBookingReturned() {
        long userId = owner.getId();
        long bookingId = 1L;
        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));

        Booking actualBooking = bookingService.getById(userId, bookingId);

        assertThat(actualBooking, is(booking));
        verify(bookingRepository).findById(bookingId);
    }

    @Test
    void getById_whenUserBookerAndNotOwner_thenBookingReturned() {
        long userId = booker.getId();
        long bookingId = 1L;
        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));

        Booking actualBooking = bookingService.getById(userId, bookingId);

        assertThat(actualBooking, is(booking));
        verify(bookingRepository).findById(bookingId);
    }

    @Test
    void getById_whenUserNotOwnerAndNotBooker_thenForbiddenExceptionThrown() {
        long userId = 100L;
        long bookingId = 0L;

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));

        assertThrows(ForbiddenException.class, () -> bookingService.getById(userId, bookingId));

        verify(bookingRepository).findById(bookingId);
    }

    @Test
    void findByBookerId_whenBookerIdIsInvalid_thanEntityNotFoundExceptionThrown() {
        long userId = 1L;
        long from = 0;
        int size = 50;
        StateFilter stateFilter = StateFilter.ALL;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.findByBookerId(userId, stateFilter, from, size));
    }

    @Test
    void findByBookerId_whenStateAll_thanListOfBookingsReturned() {
        StateFilter stateFilter = StateFilter.ALL;
        long userId = 1L;
        long from = 0;
        int size = 50;

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> actualBookings = bookingService.findByBookerId(userId, stateFilter, from, size);

        assertThat(actualBookings, contains(booking));
        verify(userRepository).findById(userId);
        verify(bookingRepository).findByBookerIdOrderByStartDesc(anyLong(), any());
    }

    @Test
    void findByBookerId_whenStateCurrent_thanListOfBookingsReturned() {
        StateFilter stateFilter = StateFilter.CURRENT;
        long userId = 1L;
        long from = 0;
        int size = 50;

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
                anyLong(), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> actualBookings = bookingService.findByBookerId(userId, stateFilter, from, size);

        assertThat(actualBookings, contains(booking));
        verify(userRepository).findById(userId);
        verify(bookingRepository).findByBookerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
                anyLong(), any(), any(), any());
    }

    @Test
    void findByBookerId_whenStatePast_thanListOfBookingsReturned() {
        StateFilter stateFilter = StateFilter.PAST;
        long userId = 1L;
        long from = 0;
        int size = 50;

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerIdAndEndLessThanOrderByStartDesc(
                anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> actualBookings = bookingService.findByBookerId(userId, stateFilter, from, size);

        assertThat(actualBookings, contains(booking));
        verify(userRepository).findById(userId);
        verify(bookingRepository).findByBookerIdAndEndLessThanOrderByStartDesc(
                anyLong(), any(), any());
    }

    @Test
    void findByBookerId_whenStateFuture_thanListOfBookingsReturned() {
        StateFilter stateFilter = StateFilter.FUTURE;
        long userId = 1L;
        long from = 0;
        int size = 50;

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerIdAndStartGreaterThanOrderByStartDesc(
                anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> actualBookings = bookingService.findByBookerId(userId, stateFilter, from, size);

        assertThat(actualBookings, contains(booking));
        verify(userRepository).findById(userId);
        verify(bookingRepository).findByBookerIdAndStartGreaterThanOrderByStartDesc(
                anyLong(), any(), any());
    }

    @ParameterizedTest
    @EnumSource(value = StateFilter.class, names = {"REJECTED", "APPROVED", "WAITING"})
    void findByBookerId_whenStateRejectedOrApprovedOrWaiting_thanListOfBookingsReturned(StateFilter stateFilter) {
        long userId = 1L;
        long from = 0;
        int size = 50;

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerIdAndStatusIsOrderByStartDesc(
                anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> actualBookings = bookingService.findByBookerId(userId, stateFilter, from, size);

        assertThat(actualBookings, contains(booking));
        verify(userRepository).findById(userId);
        verify(bookingRepository).findByBookerIdAndStatusIsOrderByStartDesc(
                anyLong(), any(), any());
    }

    @Test
    void findByItemOwner_whenOwnerIdIsInvalid_thanEntityNotFoundExceptionThrown() {
        long userId = 1L;
        long from = 0;
        int size = 50;
        StateFilter stateFilter = StateFilter.ALL;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.findByItemOwner(userId, stateFilter, from, size));
    }

    @Test
    void findByItemOwner_whenStateAll_thanListOfBookingsReturned() {
        StateFilter stateFilter = StateFilter.ALL;
        long userId = 1L;
        long from = 0;
        int size = 50;

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findByItemOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> actualBookings = bookingService.findByItemOwner(userId, stateFilter, from, size);

        assertThat(actualBookings, contains(booking));
        verify(userRepository).findById(userId);
        verify(bookingRepository).findByItemOwnerIdOrderByStartDesc(anyLong(), any());
    }

    @Test
    void findByItemOwner_whenStateCurrent_thanListOfBookingsReturned() {
        StateFilter stateFilter = StateFilter.CURRENT;
        long userId = 1L;
        long from = 0;
        int size = 50;

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
                anyLong(), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> actualBookings = bookingService.findByItemOwner(userId, stateFilter, from, size);

        assertThat(actualBookings, contains(booking));
        verify(userRepository).findById(userId);
        verify(bookingRepository).findByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
                anyLong(), any(), any(), any());
    }

    @Test
    void findByItemOwner_whenStatePast_thanListOfBookingsReturned() {
        StateFilter stateFilter = StateFilter.PAST;
        long userId = 1L;
        long from = 0;
        int size = 50;

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findByItemOwnerIdAndEndLessThanOrderByStartDesc(
                anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> actualBookings = bookingService.findByItemOwner(userId, stateFilter, from, size);

        assertThat(actualBookings, contains(booking));
        verify(userRepository).findById(userId);
        verify(bookingRepository).findByItemOwnerIdAndEndLessThanOrderByStartDesc(
                anyLong(), any(), any());
    }

    @Test
    void findByItemOwner_whenStateFuture_thanListOfBookingsReturned() {
        StateFilter stateFilter = StateFilter.FUTURE;
        long userId = 1L;
        long from = 0;
        int size = 50;

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findByItemOwnerIdAndStartGreaterThanOrderByStartDesc(
                anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> actualBookings = bookingService.findByItemOwner(userId, stateFilter, from, size);

        assertThat(actualBookings, contains(booking));
        verify(userRepository).findById(userId);
        verify(bookingRepository).findByItemOwnerIdAndStartGreaterThanOrderByStartDesc(
                anyLong(), any(), any());
    }

    @ParameterizedTest
    @EnumSource(value = StateFilter.class, names = {"REJECTED", "APPROVED", "WAITING"})
    void findByItemOwner_whenStateRejectedOrApprovedOrWaiting_thanListOfBookingsReturned(StateFilter stateFilter) {
        long userId = 1L;
        long from = 0;
        int size = 50;

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findByItemOwnerIdAndStatusIsOrderByStartDesc(
                anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> actualBookings = bookingService.findByItemOwner(userId, stateFilter, from, size);

        assertThat(actualBookings, contains(booking));
        verify(userRepository).findById(userId);
        verify(bookingRepository).findByItemOwnerIdAndStatusIsOrderByStartDesc(
                anyLong(), any(), any());
    }

    @Test
    void create_whenUserIdIsInvalid_thanEntityNotFoundExceptionThrown() {
        long userId = 0L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.create(userId, booking));

        verify(userRepository).findById(userId);
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void create_whenItemIdIsInvalid_thanEntityNotFoundExceptionThrown() {
        long userId = 0L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.create(userId, booking));

        verify(userRepository).findById(userId);
        verify(itemRepository).findById(anyLong());
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void create_whenItemNotAvailable_thanBookingExceptionThrown() {
        long userId = 0L;
        item.setAvailable(false);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(itemRepository.findById(item.getId()))
                .thenReturn(Optional.of(item));

        assertThrows(BookingException.class,
                () -> bookingService.create(userId, booking));

        verify(userRepository).findById(userId);
        verify(itemRepository).findById(item.getId());
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void create_whenOwnerTriesToBook_thanForbiddenExceptionThrown() {
        long userId = owner.getId();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(itemRepository.findById(item.getId()))
                .thenReturn(Optional.of(item));

        assertThrows(ForbiddenException.class,
                () -> bookingService.create(userId, booking));

        verify(userRepository).findById(userId);
        verify(itemRepository).findById(item.getId());
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void create_whenFinishBeforeStart_thanBookingExceptionThrown() {
        long userId = booker.getId();
        booking.setStart(LocalDateTime.of(2022, 1, 1, 0, 0));
        booking.setEnd(LocalDateTime.of(2021, 1, 1, 0, 0));

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(itemRepository.findById(item.getId()))
                .thenReturn(Optional.of(item));

        assertThrows(BookingException.class,
                () -> bookingService.create(userId, booking));

        verify(userRepository).findById(userId);
        verify(itemRepository).findById(item.getId());
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void create_whenAllValid_thanEntityNotFoundExceptionThrown() {
        long userId = booker.getId();
        booking.setStart(LocalDateTime.of(2022, 1, 1, 0, 0));
        booking.setEnd(LocalDateTime.of(2023, 1, 1, 0, 0));

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(itemRepository.findById(item.getId()))
                .thenReturn(Optional.of(item));

        bookingService.create(userId, booking);

        verify(userRepository).findById(userId);
        verify(itemRepository).findById(item.getId());
        verify(bookingRepository).save(booking);
    }

    @Test
    void setApproved_whenInvalidBookingId_thenEntityNotFoundExceptionThrown() {
        long bookingId = 0L;
        long userId = 1L;
        boolean approved = true;
        Status currentStatus = booking.getStatus();

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.setApproved(userId, bookingId, approved));

        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository, never()).save(booking);
        assertThat(booking.getStatus(), equalTo(currentStatus));
    }

    @Test
    void setApproved_whenInvalidUserId_thenEntityNotFoundExceptionThrown() {
        long bookingId = 1L;
        long userId = 1L;
        boolean approved = true;
        Status currentStatus = booking.getStatus();

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.setApproved(userId, bookingId, approved));

        verify(bookingRepository).findById(bookingId);
        verify(userRepository).findById(userId);
        verify(bookingRepository, never()).save(booking);
        assertThat(booking.getStatus(), equalTo(currentStatus));
    }

    @Test
    void setApproved_whenUserNotOwner_thenForbiddenExceptionThrown() {
        long bookingId = 1L;
        long userId = booker.getId();
        boolean approved = true;
        Status currentStatus = booking.getStatus();

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));

        assertThrows(ForbiddenException.class,
                () -> bookingService.setApproved(userId, bookingId, approved));

        verify(bookingRepository).findById(bookingId);
        verify(userRepository).findById(userId);
        verify(bookingRepository, never()).save(booking);
        assertThat(booking.getStatus(), equalTo(currentStatus));
    }

    @Test
    void setApproved_whenStatusNotWaiting_thenBookingExceptionThrown() {
        long bookingId = 1L;
        long userId = owner.getId();
        boolean approved = true;
        booking.setStatus(Status.REJECTED);
        Status currentStatus = booking.getStatus();

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));

        assertThrows(BookingException.class,
                () -> bookingService.setApproved(userId, bookingId, approved));

        verify(bookingRepository).findById(bookingId);
        verify(userRepository).findById(userId);
        verify(bookingRepository, never()).save(booking);
        assertThat(booking.getStatus(), equalTo(currentStatus));
    }

    @Test
    void setApproved_whenAllValid_thenStatusChanged() {
        long bookingId = 1L;
        long userId = owner.getId();
        boolean approved = true;
        booking.setStatus(Status.WAITING);

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.save(booking))
                .thenReturn(booking);

        Booking changedBooking = bookingService.setApproved(userId, bookingId, approved);

        verify(bookingRepository).findById(bookingId);
        verify(userRepository).findById(userId);
        verify(bookingRepository).save(booking);
        assertThat(changedBooking.getStatus(), equalTo(Status.APPROVED));
    }
}