package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

//    Optional<User> getByEmail(String email);
}
