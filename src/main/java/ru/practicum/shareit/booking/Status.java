package ru.practicum.shareit.booking;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Статус бронирования")
public enum Status {

    WAITING, APPROVED, REJECTED
}
