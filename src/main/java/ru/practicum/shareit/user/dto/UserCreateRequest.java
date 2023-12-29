package ru.practicum.shareit.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Параметр запроса для создания {@link User}.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Параметры для создания пользователя")
public class UserCreateRequest {

    @NotBlank(message = "Имя пользователя не может пустым.")
    @Schema(description = "Имя пользователя", example = "Максим")
    String name;

    @NotBlank(message = "Электронная почта не может быть пустой.")
    @Email(message = "Электронная почта не соответствует формату \"user@mail.ru\".")
    @Schema(description = "Электронная почта", example = "user@mail.com")
    String email;
}