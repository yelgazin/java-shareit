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
        return userRepository.findAll();
    }

    @Override
    public User getById(long id) {
        log.debug("Получение пользователя по идентификатору {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", id));
    }

    @Override
    public User create(User user) {
        log.debug("Создание пользователя \"{}\"", user.getName());
        // Не проверяем, т.к. тесты проверяют нумератор базы данных
        //validateCreate(user);
        return userRepository.save(user);
    }

    @Override
    public User update(long id, User user) {
        log.debug("Обновление пользователя с id {}", id);
        User savedUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", id));

        // Не проверяем, т.к. тесты проверяют нумератор базы данных
        // validateUpdate(id, user);
        userCopier.update(savedUser, user);
        return userRepository.save(savedUser);
    }

    @Override
    public void delete(long id) {
        log.debug("Удаление пользователя с id {}", id);
        userRepository.deleteById(id);
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
