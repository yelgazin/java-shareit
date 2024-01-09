package ru.practicum.shareit.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserConverter;
import ru.practicum.shareit.user.dto.UserCreateRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdateRequest;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер для {@link User}.
 */
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
@Tag(name = "Users", description = "Управление пользователями")
public class UserController {

    private final UserConverter userConverter;
    private final UserService userService;

    /**
     * Получение всех пользователей.
     * @return список пользователей.
     */
    @GetMapping
    @Operation(summary = "Получение всех пользователей")
    public List<UserResponse> getAll() {
        return userConverter.convert(userService.getAll());
    }

    /**
     * Получение пользователя по идентификатору.
     * @param id идентификатор пользователя.
     * @return найденные пользователи.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя по идентификатору")
    public UserResponse getById(@PathVariable long id) {
        return userConverter.convert(userService.getById(id));
    }

    /**
     * Создание пользователя.
     * @param userCreateRequest параметры для создания пользователя.
     * @return созданный пользователь.
     */
    @PostMapping
    @Operation(summary = "Создание пользователя")
    public UserResponse create(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        return userConverter.convert(
                userService.create(userConverter.convertCreateRequestDto(userCreateRequest)));
    }

    /**
     * Обновление пользователя.
     * @param id идентификатор пользователя.
     * @param userUpdateRequest параметры для изменения.
     * @return измененный пользователь.
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Обновление пользователя")
    public UserResponse update(@PathVariable long id,
                               @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        return userConverter.convert(
                userService.update(id, userConverter.convertUpdateRequestDto(userUpdateRequest)));
    }

    /**
     * Удаление пользователя.
     * @param id идентификатор пользователя.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя")
    public void delete(@PathVariable long id) {
        userService.delete(id);
    }
}
