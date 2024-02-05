package ru.practicum.shareit.server.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.user.entity.User;

/**
 * Параметры ответа для {@link User}.
 */
@Data
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
