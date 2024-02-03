package ru.practicum.shareit.gateway.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.gateway.user.client.UserClient;
import ru.practicum.shareit.server.user.dto.UserCreateRequest;
import ru.practicum.shareit.server.user.dto.UserUpdateRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserClient userClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> getAll() {
        log.debug("Получение всех пользователей.");
        return userClient.getAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> getById(long id) {
        log.debug("Получение пользователя по идентфиикатору. ИД получаемого пользователя: {}.", id);
        return userClient.getById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> create(UserCreateRequest userCreateRequest) {
        log.debug("Создание пользователя.");
        return userClient.create(userCreateRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> update(long id, UserUpdateRequest userUpdateRequest) {
        log.debug("Обновление пользователя. ИД обновляемого пользователя: {}.", id);
        return userClient.update(id, userUpdateRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> delete(long id) {
        log.debug("Удаление пользователя. ИД удаляемого пользователя: {}.", id);
        return userClient.delete(id);
    }
}
