package ru.practicum.shareit.server.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.server.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getByEmail(String email);
}
