package ru.practicum.shareit.gateway.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.gateway.booking.client.BookingClient;
import ru.practicum.shareit.gateway.helper.ObjectGenerator;
import ru.practicum.shareit.server.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.server.booking.dto.BookingResponse;
import ru.practicum.shareit.server.booking.entity.StateFilter;
import ru.practicum.shareit.server.booking.entity.Status;
import ru.practicum.shareit.server.booking.mapper.BookingConverter;
import ru.practicum.shareit.server.item.dto.CommentCreateRequest;
import ru.practicum.shareit.server.item.dto.CommentResponse;
import ru.practicum.shareit.server.item.dto.ItemCreateRequest;
import ru.practicum.shareit.server.item.dto.ItemResponse;
import ru.practicum.shareit.server.user.dto.UserResponse;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BookingControllerImpl.class, BookingConverter.class, ObjectGenerator.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingControllerImplIT {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ObjectGenerator objectGenerator;

    @MockBean
    private BookingClient bookingClient;

    private LocalDateTime currentDateTime;
    private BookingCreateRequest bookingCreateRequest;
    private BookingResponse bookingResponse;
    private ItemCreateRequest itemCreateRequest;
    private CommentCreateRequest commentCreateRequest;
    private UserResponse bookerResponse;
    private ItemResponse itemResponse;
    private CommentResponse commentResponse;
    private ItemResponse.BookingView lastBookingView;

    @BeforeEach
    void setUp() {
        currentDateTime = LocalDateTime.now();

        itemCreateRequest = objectGenerator.next(ItemCreateRequest.class);
        bookingCreateRequest = objectGenerator.next(BookingCreateRequest.class);
        commentCreateRequest = objectGenerator.next(CommentCreateRequest.class);

        bookerResponse = new UserResponse();
        bookerResponse.setId(1L);
        bookerResponse.setName("Booker Name");
        bookerResponse.setEmail("booker@mail.com");

        commentResponse = new CommentResponse();
        commentResponse.setId(1L);
        commentResponse.setCreated(LocalDateTime.of(2021, 12, 23, 23, 59, 0));
        commentResponse.setAuthorName("Comment author name");
        commentResponse.setText(commentCreateRequest.getText());

        lastBookingView = new ItemResponse.BookingView();
        lastBookingView.setId(1L);
        lastBookingView.setBookerId(1L);

        itemResponse = new ItemResponse();
        itemResponse.setId(1L);
        itemResponse.setName(itemCreateRequest.getName());
        itemResponse.setDescription(itemCreateRequest.getDescription());
        itemResponse.setAvailable(itemCreateRequest.getAvailable());
        itemResponse.setComments(Set.of(commentResponse));
        itemResponse.setRequestId(1L);
        itemResponse.setLastBooking(lastBookingView);
        itemResponse.setNextBooking(null);

        bookingResponse = new BookingResponse();
        bookingResponse.setId(1L);
        bookingResponse.setStatus(Status.APPROVED);
        bookingResponse.setStart(bookingCreateRequest.getStart());
        bookingResponse.setEnd(bookingCreateRequest.getEnd());
        bookingResponse.setBooker(bookerResponse);
        bookingResponse.setItem(itemResponse);
    }


    @SneakyThrows
    @Test
    void create_whenValidBookingCreateRequest_thenBookingResponseReturned() {
        long userId = 1L;
        bookingCreateRequest.setStart(currentDateTime.plusDays(1));
        bookingCreateRequest.setEnd(currentDateTime.plusDays(2));
        bookingCreateRequest.setItemId(1L);

        when(bookingClient.create(anyLong(), any()))
                .thenReturn(ResponseEntity.ok(bookingResponse));

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(bookingCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.item.id", is(itemResponse.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(itemResponse.getName())))
                .andExpect(jsonPath("$.item.description", is(itemResponse.getDescription())))
                .andExpect(jsonPath("$.item.available", is(itemResponse.getAvailable())))
                .andExpect(jsonPath("$.item.requestId").hasJsonPath())
                .andExpect(jsonPath("$.item.comments").hasJsonPath())
                .andExpect(jsonPath("$.item.nextBooking").hasJsonPath())
                .andExpect(jsonPath("$.item.lastBooking").hasJsonPath())
                .andExpect(jsonPath("$.booker").hasJsonPath())
                .andExpect(jsonPath("$.status").hasJsonPath())
                .andExpect(jsonPath("$.start").hasJsonPath())
                .andExpect(jsonPath("$.end").hasJsonPath());

        verify(bookingClient).create(anyLong(), any());
    }


    @SneakyThrows
    @Test
    void create_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage =
                "Required request header 'X-Sharer-User-Id' for method parameter type long is not present";
        bookingCreateRequest.setStart(currentDateTime.plusDays(1));
        bookingCreateRequest.setEnd(currentDateTime.plusDays(2));
        bookingCreateRequest.setItemId(1L);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(bookingCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(errorMessage)));

        verify(bookingClient, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenItemIdIsNull_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage = "Идентификатор вещи не может быть пустым.";
        bookingCreateRequest.setStart(currentDateTime.plusDays(1));
        bookingCreateRequest.setEnd(currentDateTime.plusDays(2));
        bookingCreateRequest.setItemId(null);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(bookingCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(bookingClient, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenStarDateIsNull_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage = "Дата начала бронирования не может быть пустой.";
        bookingCreateRequest.setStart(null);
        bookingCreateRequest.setEnd(currentDateTime.plusDays(2));
        bookingCreateRequest.setItemId(1L);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(bookingCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(bookingClient, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenStartDateInPast_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage = "Дата начала бронирования должна быть больше или равна текущей даты.";
        bookingCreateRequest.setStart(currentDateTime.minusDays(1));
        bookingCreateRequest.setEnd(currentDateTime.plusDays(2));
        bookingCreateRequest.setItemId(1L);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(bookingCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(bookingClient, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenEndIsNull_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage = "Дата окончания бронирования не может быть пустой.";
        bookingCreateRequest.setStart(currentDateTime.plusDays(1));
        bookingCreateRequest.setEnd(null);
        bookingCreateRequest.setItemId(1L);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(bookingCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(bookingClient, never()).create(anyLong(), any());
    }


    @SneakyThrows
    @Test
    void create_whenEndIsCurrentDateTime_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage = "Дата окончания бронирования должна быть больше текущей даты.";
        bookingCreateRequest.setStart(currentDateTime.plusDays(1));
        bookingCreateRequest.setEnd(currentDateTime);
        bookingCreateRequest.setItemId(1L);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(bookingCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(bookingClient, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void approve_whenValidRequest_thenOkStatusReturned() {
        long userId = 1L;
        long bookingId = 1L;
        boolean approved = true;

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", Boolean.toString(approved)))
                .andExpect(status().isOk());

        verify(bookingClient).approve(userId, bookingId, approved);
    }

    @SneakyThrows
    @Test
    void approve_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = 1L;
        long bookingId = 1L;
        boolean approved = true;

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("approved", Boolean.toString(approved)))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).approve(userId, bookingId, approved);
    }

    @SneakyThrows
    @Test
    void getById_whenValidRequest_thenBookingResponseReturned() {
        long userId = 1L;
        long bookingId = 1L;

        when(bookingClient.getById(userId, bookingId))
                .thenReturn(ResponseEntity.ok(bookingResponse));

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.item.id", is(itemResponse.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(itemResponse.getName())))
                .andExpect(jsonPath("$.item.description", is(itemResponse.getDescription())))
                .andExpect(jsonPath("$.item.available", is(itemResponse.getAvailable())))
                .andExpect(jsonPath("$.item.requestId").hasJsonPath())
                .andExpect(jsonPath("$.item.comments").hasJsonPath())
                .andExpect(jsonPath("$.item.nextBooking").hasJsonPath())
                .andExpect(jsonPath("$.item.lastBooking").hasJsonPath())
                .andExpect(jsonPath("$.booker").hasJsonPath())
                .andExpect(jsonPath("$.status").hasJsonPath())
                .andExpect(jsonPath("$.start").hasJsonPath())
                .andExpect(jsonPath("$.end").hasJsonPath());

        verify(bookingClient).getById(userId, bookingId);
    }

    @SneakyThrows
    @Test
    void getById_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = 1L;
        long bookingId = 1L;

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).getById(userId, bookingId);
    }

    @SneakyThrows
    @Test
    void getByBookerId_whenValidRequest_thenListOfBookingResponseReturned() {
        long userId = 0L;
        StateFilter stateFilter = StateFilter.APPROVED;
        long from = 0;
        int size = 50;

        when(bookingClient.getByBookerId(userId, stateFilter, from, size))
                .thenReturn(ResponseEntity.ok(List.of(bookingResponse)));

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", stateFilter.toString())
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(itemResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(itemResponse.getName())))
                .andExpect(jsonPath("$[0].item.description", is(itemResponse.getDescription())))
                .andExpect(jsonPath("$[0].item.available", is(itemResponse.getAvailable())))
                .andExpect(jsonPath("$[0].item.requestId").hasJsonPath())
                .andExpect(jsonPath("$[0].item.comments").hasJsonPath())
                .andExpect(jsonPath("$[0].item.nextBooking").hasJsonPath())
                .andExpect(jsonPath("$[0].item.lastBooking").hasJsonPath())
                .andExpect(jsonPath("$[0].booker").hasJsonPath())
                .andExpect(jsonPath("$[0].status").hasJsonPath())
                .andExpect(jsonPath("$[0].start").hasJsonPath())
                .andExpect(jsonPath("$[0].end").hasJsonPath());

        verify(bookingClient).getByBookerId(userId, stateFilter, from, size);
    }

    @SneakyThrows
    @Test
    void getByBookerId_whenInvalidStateInParameter_thenBadRequestReturned() {
        long userId = 0L;
        String invalidState = "RANDOM STATE";
        long from = 0;
        int size = 50;
        String errorMessage = "Unknown state";

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", invalidState)
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(bookingClient, never()).getByBookerId(anyLong(), any(), anyLong(), anyInt());
    }

    @SneakyThrows
    @Test
    void getByBookerId_whenNegativeFromInParameter_thenBadRequestReturned() {
        long userId = 0L;
        long from = -1;
        int size = 50;
        StateFilter stateFilter = StateFilter.APPROVED;
        String errorMessage = "must be greater than or equal to 0";

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", stateFilter.toString())
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(bookingClient, never()).getByBookerId(anyLong(), any(), anyLong(), anyInt());
    }

    @SneakyThrows
    @Test
    void getByBookerId_whenZeroSizeInParameter_thenBadRequestReturned() {
        long userId = 0L;
        long from = 0;
        int size = 0;
        StateFilter stateFilter = StateFilter.APPROVED;
        String errorMessage = "must be greater than 0";

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", stateFilter.toString())
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(bookingClient, never()).getByBookerId(anyLong(), any(), anyLong(), anyInt());
    }

    @SneakyThrows
    @Test
    void getByOwner_whenValidRequest_thenListOfBookingResponseReturned() {
        long userId = 0L;
        StateFilter stateFilter = StateFilter.APPROVED;
        long from = 0;
        int size = 50;

        when(bookingClient.getByOwner(userId, stateFilter, from, size))
                .thenReturn(ResponseEntity.ok(List.of(bookingResponse)));

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", stateFilter.toString())
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(itemResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(itemResponse.getName())))
                .andExpect(jsonPath("$[0].item.description", is(itemResponse.getDescription())))
                .andExpect(jsonPath("$[0].item.available", is(itemResponse.getAvailable())))
                .andExpect(jsonPath("$[0].item.requestId").hasJsonPath())
                .andExpect(jsonPath("$[0].item.comments").hasJsonPath())
                .andExpect(jsonPath("$[0].item.nextBooking").hasJsonPath())
                .andExpect(jsonPath("$[0].item.lastBooking").hasJsonPath())
                .andExpect(jsonPath("$[0].booker").hasJsonPath())
                .andExpect(jsonPath("$[0].status").hasJsonPath())
                .andExpect(jsonPath("$[0].start").hasJsonPath())
                .andExpect(jsonPath("$[0].end").hasJsonPath());

        verify(bookingClient).getByOwner(userId, stateFilter, from, size);
    }

    @SneakyThrows
    @Test
    void getByOwner_whenInvalidStateInParameter_thenBadRequestReturned() {
        long userId = 0L;
        String invalidState = "RANDOM STATE";
        long from = 0;
        int size = 50;
        String errorMessage = "Unknown state";

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", invalidState)
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(bookingClient, never()).getByOwner(anyLong(), any(), anyLong(), anyInt());
    }

    @SneakyThrows
    @Test
    void getByOwner_whenNegativeFromInParameter_thenBadRequestReturned() {
        long userId = 0L;
        long from = -1;
        int size = 50;
        StateFilter stateFilter = StateFilter.APPROVED;
        String errorMessage = "must be greater than or equal to 0";

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", stateFilter.toString())
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(bookingClient, never()).getByOwner(anyLong(), any(), anyLong(), anyInt());
    }

    @SneakyThrows
    @Test
    void getByOwner_whenZeroSizeInParameter_thenBadRequestReturned() {
        long userId = 0L;
        long from = 0;
        int size = 0;
        StateFilter stateFilter = StateFilter.APPROVED;
        String errorMessage = "must be greater than 0";

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", stateFilter.toString())
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(bookingClient, never()).getByOwner(anyLong(), any(), anyLong(), anyInt());
    }
}