package ru.practicum.shareit.gateway.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.user.dto.UserCreateRequest;
import ru.practicum.shareit.server.user.dto.UserUpdateRequest;
import ru.practicum.shareit.server.user.entity.User;

/**
 * Контроллер для {@link User}.
 */
@Validated
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
    ResponseEntity<Object> getAll();

    /**
     * Получение пользователя по идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return найденные пользователи.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя по идентификатору")
    ResponseEntity<Object> getById(@PathVariable long id);

    /**
     * Создание пользователя.
     *
     * @param userCreateRequest параметры для создания пользователя.
     * @return созданный пользователь.
     */
    @PostMapping
    @Operation(summary = "Создание пользователя")
    ResponseEntity<Object> create(@Validated @RequestBody UserCreateRequest userCreateRequest);

    /**
     * Обновление пользователя.
     *
     * @param id                идентификатор пользователя.
     * @param userUpdateRequest параметры для изменения.
     * @return измененный пользователь.
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Обновление пользователя")
    ResponseEntity<Object> update(@PathVariable long id, @Validated @RequestBody UserUpdateRequest userUpdateRequest);

    /**
     * Удаление пользователя.
     *
     * @param id идентификатор пользователя.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя")
    ResponseEntity<Object> delete(@PathVariable long id);
}
