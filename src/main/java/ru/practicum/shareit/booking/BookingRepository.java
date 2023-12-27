package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(long bookerId);

    List<Booking> findByBookerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(long bookerId,
                                                                            LocalDateTime greaterTime,
                                                                            LocalDateTime lessTime);

    List<Booking> findByBookerIdAndEndLessThanOrderByStartDesc(long bookerId, LocalDateTime currentTime);

    List<Booking> findByBookerIdAndStartGreaterThanOrderByStartDesc(long bookerId, LocalDateTime currentTime);

    List<Booking> findByBookerIdAndStartLessThanAndStatusIsOrderByStartDesc(long bookerId, LocalDateTime currentTime,
                                                                            Status status);

    List<Booking> findByBookerIdAndStatusIsOrderByStartDesc(long bookerId, Status status);

    List<Booking> findByItemOwnerIdOrderByStartDesc(long bookerId);

    List<Booking> findByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(long ownerId,
                                                                                            LocalDateTime greaterTime,
                                                                                            LocalDateTime lessTime);

    List<Booking> findByItemOwnerIdAndEndLessThanOrderByStartDesc(long ownerId, LocalDateTime currentTime);

    List<Booking> findByItemOwnerIdAndStartGreaterThanOrderByStartDesc(long ownerId, LocalDateTime currentTime);

    List<Booking> findByItemOwnerIdAndStatusIsOrderByStartDesc(long ownerId, Status status);

    List<Booking> findByItemId(long itemId);
}
