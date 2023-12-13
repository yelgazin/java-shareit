package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.EntityAlreadyExistsException;
import ru.practicum.shareit.common.exception.EntityNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCopier userCopier;

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(long id) {
        return userRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", id));
    }

    @Override
    public User create(User user) {
        validateCreate(user);
        return userRepository.create(user);
    }

    @Override
    public User update(long id, User user) {
        User savedUser = userRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", id));
        validateUpdate(id, user);
        userCopier.update(savedUser, user);
        return userRepository.update(id, savedUser);
    }

    @Override
    public void delete(long id) {
        userRepository.delete(id);
    }

    private void validateCreate(User user) {
        String email = user.getEmail();
        if (email != null) {
            userRepository.getByEmail(email)
                    .map(User::getId)
                    .ifPresent((e) -> {
                        throw new EntityAlreadyExistsException("Электронный адрес %s уже используется.", email);
                    });
        }
    }

    private void validateUpdate(long entityId, User user) {
        String updatedEmail = user.getEmail();
        if (updatedEmail != null) {
            userRepository.getByEmail(updatedEmail)
                    .map(User::getId)
                    .filter(item -> !item.equals(entityId))
                    .ifPresent((e) -> {
                        throw new EntityAlreadyExistsException("Электронный адрес %s уже используется.", updatedEmail);
                    });
        }
    }
}
