package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    @NotBlank(message = "Имя пользователя не может пустым.")
    String name;

    @NotBlank(message = "Электронная почта не может быть пустой.")
    @Email(message = "Электронная почта не соответствует формату \"user@mail.ru\".")
    String email;
}