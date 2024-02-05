package ru.practicum.shareit.server.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.server.booking.entity.Booking;
import ru.practicum.shareit.server.booking.entity.Status;

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

    @Query(value = "select t1.* " +
            "from (" +
            "    select b.*, row_number() over(partition by item_id order by start_time desc) non_field_counter " +
            "    from booking b" +
            "    where b.item_id in :itemIds and b.start_time < :currentDateTime and b.status = :status" +
            ") as t1 " +
            "WHERE t1.non_field_counter = 1", nativeQuery = true)
    List<Booking> findLastBookings(List<Long> itemIds, String status, LocalDateTime currentDateTime);

    @Query(value = "select t1.* " +
            "from (" +
            "    select b.*, row_number() over(partition by item_id order by start_time asc) non_field_counter " +
            "    from booking b" +
            "    where b.item_id in :itemIds and b.start_time >= :currentDateTime and b.status = :status" +
            ") as t1 " +
            "WHERE t1.non_field_counter = 1", nativeQuery = true)
    List<Booking> findNextBookings(List<Long> itemIds, String status, LocalDateTime currentDateTime);
}
