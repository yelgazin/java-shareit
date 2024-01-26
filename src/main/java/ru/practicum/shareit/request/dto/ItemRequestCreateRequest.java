package ru.practicum.shareit.request.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;

/**
 * Параметры запроса для создания {@link ItemRequest}.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Параметры для запроса вещи")
public class ItemRequestCreateRequest {

    @NotBlank(message = "Описание не может быть пустым.")
    @Schema(description = "Описание вещий в запросе", example = "Ищу ручную дрель со сменным аккумулятором")
    String description;
}
