package ru.practicum.shareit.server.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.user.dto.UserCreateRequest;
import ru.practicum.shareit.server.user.dto.UserResponse;
import ru.practicum.shareit.server.user.dto.UserUpdateRequest;
import ru.practicum.shareit.server.user.entity.User;

import java.util.List;

/**
 * Контроллер для {@link User}.
 */
@RequestMapping(path = "/users")
@Tag(name = "Users", description = "Управление пользователями")
public interface UserController {

    /**
     * Получение всех пользователей.
     *
     * @return список пользователей.
     */
    @GetMapping
    @Operation(summary = "Получение всех пользователей")
    List<UserResponse> getAll();

    /**
     * Получение пользователя по идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return найденные пользователи.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя по идентификатору")
    UserResponse getById(@PathVariable long id);

    /**
     * Создание пользователя.
     *
     * @param userCreateRequest параметры для создания пользователя.
     * @return созданный пользователь.
     */
    @PostMapping
    @Operation(summary = "Создание пользователя")
    UserResponse create(@RequestBody UserCreateRequest userCreateRequest);

    /**
     * Обновление пользователя.
     *
     * @param id                идентификатор пользователя.
     * @param userUpdateRequest параметры для изменения.
     * @return измененный пользователь.
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Обновление пользователя")
    UserResponse update(@PathVariable long id, @RequestBody UserUpdateRequest userUpdateRequest);

    /**
     * Удаление пользователя.
     *
     * @param id идентификатор пользователя.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя")
    void delete(@PathVariable long id);
}
