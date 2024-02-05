package ru.practicum.shareit.server.request.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.request.entity.ItemRequest;

import javax.validation.constraints.NotBlank;

/**
 * Параметры запроса для создания {@link ItemRequest}.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Параметры для запроса вещи")
public class ItemRequestCreateRequest {

    @NotBlank(message = "Описание не может быть пустым.")
    @Schema(description = "Описание вещий в запросе", example = "Ищу ручную дрель со сменным аккумулятором")
    String description;
}
