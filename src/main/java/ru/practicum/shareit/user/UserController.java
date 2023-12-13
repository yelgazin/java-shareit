package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserConverter;
import ru.practicum.shareit.user.dto.UserCreateRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdateRequest;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    private final UserConverter userConverter;
    private final UserService userService;

    @GetMapping
    public List<UserResponse> getAll() {
        return userConverter.convert(userService.getAll());
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable long id) {
        return userConverter.convert(userService.getById(id));
    }

    @PostMapping
    public UserResponse create(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        return userConverter.convert(
                userService.create(userConverter.convertCreateRequestDto(userCreateRequest)));
    }

    @PatchMapping("/{id}")
    public UserResponse update(@PathVariable long id,
                               @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        return userConverter.convert(
                userService.update(id, userConverter.convertUpdateRequestDto(userUpdateRequest)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        userService.delete(id);
    }
}
