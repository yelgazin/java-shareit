package ru.practicum.shareit.server.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.common.exception.EntityNotFoundException;
import ru.practicum.shareit.server.user.repository.UserRepository;
import ru.practicum.shareit.server.user.entity.User;
import ru.practicum.shareit.server.user.mapper.UserCopier;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

    @Transactional
    @Override
    public User create(User user) {
        log.debug("Создание пользователя \"{}\"", user.getName());
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User update(long id, User user) {
        log.debug("Обновление пользователя с id {}", id);
        User savedUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", id));
        userCopier.update(savedUser, user);
        return userRepository.save(savedUser);
    }

    @Transactional
    @Override
    public void delete(long id) {
        log.debug("Удаление пользователя с id {}", id);
        userRepository.deleteById(id);
    }
}