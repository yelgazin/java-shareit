package ru.practicum.shareit.server.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.server.user.entity.User;
import ru.practicum.shareit.server.user.service.UserService;
import ru.practicum.shareit.server.user.mapper.UserConverter;
import ru.practicum.shareit.server.user.dto.UserCreateRequest;
import ru.practicum.shareit.server.user.dto.UserResponse;
import ru.practicum.shareit.server.user.dto.UserUpdateRequest;

import java.util.List;

/**
 * Имплементация контроллера для {@link User}.
 */
@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserConverter userConverter;
    private final UserService userService;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserResponse> getAll() {
        return userConverter.convert(userService.getAll());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserResponse getById(long id) {
        return userConverter.convert(userService.getById(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserResponse create(UserCreateRequest userCreateRequest) {
        return userConverter.convert(
                userService.create(userConverter.convertCreateRequestDto(userCreateRequest)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserResponse update(long id, UserUpdateRequest userUpdateRequest) {
        return userConverter.convert(
                userService.update(id, userConverter.convertUpdateRequestDto(userUpdateRequest)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(long id) {
        userService.delete(id);
    }
}
