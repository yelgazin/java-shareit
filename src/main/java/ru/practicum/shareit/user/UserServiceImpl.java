package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.EntityAlreadyExistsException;
import ru.practicum.shareit.common.exception.EntityNotFoundException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCopier userCopier;

    @Override
    public List<User> getAll() {
        log.debug("Получение списка всех пользователей");
        return userRepository.getAll();
    }

    @Override
    public User getById(long id) {
        log.debug("Получение пользователя по идентификатору {}", id);
        return userRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", id));
    }

    @Override
    public User create(User user) {
        log.debug("Создание пользователя \"{}\"", user.getName());
        validateCreate(user);
        return userRepository.create(user);
    }

    @Override
    public User update(long id, User user) {
        log.debug("Обновление пользователя с id {}", id);
        User savedUser = userRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", id));
        validateUpdate(id, user);
        userCopier.update(savedUser, user);
        return userRepository.update(id, savedUser);
    }

    @Override
    public void delete(long id) {
        log.debug("Удаление пользователя с id {}", id);
        userRepository.delete(id);
    }

    private void validateCreate(User user) {
        log.debug("Валидация пользователя \"{}\" при создании", user.getName());
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
        log.debug("Валидация пользователя с id {} при обновлении", entityId);
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
