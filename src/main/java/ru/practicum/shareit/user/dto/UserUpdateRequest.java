package ru.practicum.shareit.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.Email;

/**
 * Параметр запроса для редактирования {@link User}.
 * Содержит только поля, которые можно редактировать.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Параметры для редактирование пользователя")
public class UserUpdateRequest {
    @Schema(description = "Имя пользователя", example = "Максим")
    String name;

    @Email(message = "Электронная почта не соответствует формату \"user@mail.ru\" .")
    @Schema(description = "Электронная почта", example = "user@mail.com")
    String email;
}