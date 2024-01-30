package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByBookerIdOrderByStartDesc(long bookerId, PageRequest pageRequest);

    Page<Booking> findByBookerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(long bookerId,
                                                                                            LocalDateTime greaterTime,
                                                                                            LocalDateTime lessTime,
                                                                                            PageRequest pageRequest);

    Page<Booking> findByBookerIdAndEndLessThanOrderByStartDesc(long bookerId, LocalDateTime currentTime,
                                                               PageRequest pageRequest);

    Page<Booking> findByBookerIdAndStartGreaterThanOrderByStartDesc(long bookerId, LocalDateTime currentTime,
                                                                    PageRequest pageRequest);

    List<Booking> findByBookerIdAndStartLessThanAndStatusIsOrderByStartDesc(long bookerId, LocalDateTime currentTime,
                                                                            Status status);

    Page<Booking> findByBookerIdAndStatusIsOrderByStartDesc(long bookerId, Status status,
                                                            PageRequest pageRequest);

    Page<Booking> findByItemOwnerIdOrderByStartDesc(long bookerId,
                                                    PageRequest pageRequest);

    Page<Booking> findByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(long ownerId,
                                                                                               LocalDateTime greaterTime,
                                                                                               LocalDateTime lessTime,
                                                                                               PageRequest pageRequest);

    Page<Booking> findByItemOwnerIdAndEndLessThanOrderByStartDesc(long ownerId, LocalDateTime currentTime,
                                                                  PageRequest pageRequest);

    Page<Booking> findByItemOwnerIdAndStartGreaterThanOrderByStartDesc(long ownerId, LocalDateTime currentTime,
                                                                       PageRequest pageRequest);

    Page<Booking> findByItemOwnerIdAndStatusIsOrderByStartDesc(long ownerId, Status status,
                                                               PageRequest pageRequest);

    Page<Booking> findByItemId(long itemId, PageRequest pageRequest);
}
