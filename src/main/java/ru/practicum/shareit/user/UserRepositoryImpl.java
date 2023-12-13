package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.common.AbstractRepository;

import java.util.Objects;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends AbstractRepository<User> implements UserRepository {

    @Override
    public Optional<User> getByEmail(String email) {
        return storage.values()
                .stream()
                .filter(user -> Objects.equals(user.getEmail(), email))
                .findAny();
    }
}
