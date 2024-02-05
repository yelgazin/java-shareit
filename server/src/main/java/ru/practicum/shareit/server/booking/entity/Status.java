package ru.practicum.shareit.server.booking.entity;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Статус бронирования")
public enum Status {

    WAITING, APPROVED, REJECTED
}
