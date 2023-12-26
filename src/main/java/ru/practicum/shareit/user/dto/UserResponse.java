package ru.practicum.shareit.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

/**
 * Параметры ответа для {@link User}.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Параметры пользователя для ответа")
public class UserResponse {

    @Schema(description = "Идентификатор пользователя", example = "1")
    Long id;

    @Schema(description = "Имя пользователя", example = "Максим")
    String name;

    @Schema(description = "Электронная почта", example = "user@mail.com")
    String email;
}
