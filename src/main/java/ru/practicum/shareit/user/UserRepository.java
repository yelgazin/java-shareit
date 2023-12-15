package ru.practicum.shareit.user;

import ru.practicum.shareit.common.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User> {

    Optional<User> getByEmail(String email);
}
