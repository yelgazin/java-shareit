package ru.practicum.shareit.gateway.item.controller;

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
import ru.practicum.shareit.gateway.helper.ObjectGenerator;
import ru.practicum.shareit.gateway.item.client.ItemClient;
import ru.practicum.shareit.server.item.dto.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ItemControllerImpl.class, ObjectGenerator.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemControllerImplIT {

    private final ObjectMapper objectMapper;
    private final ObjectGenerator objectGenerator;
    private final MockMvc mockMvc;

    @MockBean
    private ItemClient itemClient;

    private ItemCreateRequest itemCreateRequest;
    private ItemUpdateRequest itemUpdateRequest;
    private ItemResponse itemResponse;
    private CommentCreateRequest commentCreateRequest;
    private CommentResponse commentResponse;
    private ItemResponse.BookingView lastBookingView;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @BeforeEach
    void setUp() {
        itemCreateRequest = objectGenerator.next(ItemCreateRequest.class);
        itemUpdateRequest = objectGenerator.next(ItemUpdateRequest.class);
        commentCreateRequest = objectGenerator.next(CommentCreateRequest.class);

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
    }

    @SneakyThrows
    @Test
    void getByUserId_whenValidRequest_thenListOfItemReturned() {
        long userId = 1L;
        long from = 0;
        int size = 50;

        when(itemClient.getByUserId(userId, from, size))
                .thenReturn(ResponseEntity.ok(List.of(itemResponse)));

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(itemResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", equalTo(itemResponse.getName())))
                .andExpect(jsonPath("$[0].description", equalTo(itemResponse.getDescription())))
                .andExpect(jsonPath("$[0].available", equalTo(itemResponse.getAvailable())))
                .andExpect(jsonPath("$[0].comments").hasJsonPath())
                .andExpect(jsonPath("$[0].comments[0].id", equalTo(commentResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].comments[0].authorName", equalTo(commentResponse.getAuthorName())))
                .andExpect(jsonPath("$[0].comments[0].text", equalTo(commentResponse.getText())))
                .andExpect(jsonPath("$[0].comments[0].created",
                        equalTo(commentResponse.getCreated().format(dateTimeFormatter))))
                .andExpect(jsonPath("$[0].requestId", equalTo(itemResponse.getRequestId()), Long.class))
                .andExpect(jsonPath("$[0].lastBooking.id",
                        equalTo(itemResponse.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].lastBooking.bookerId",
                        equalTo(itemResponse.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$[0].nextBooking", equalTo(itemResponse.getNextBooking())));

        verify(itemClient).getByUserId(userId, from, size);
    }

    @SneakyThrows
    @Test
    void getByUserId_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = 1L;
        long from = 0;
        int size = 50;
        String errorMessage =
                "Required request header 'X-Sharer-User-Id' for method parameter type long is not present";

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(errorMessage)));

        verify(itemClient, never()).getByUserId(userId, from, size);
    }

    @SneakyThrows
    @Test
    void getByUserId_whenNegativeFromInParameter_thenBadRequestReturned() {
        long userId = 1L;
        long from = -1;
        int size = 50;
        String errorMessage = "must be greater than or equal to 0";

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(itemClient, never()).getByUserId(userId, from, size);
    }

    @SneakyThrows
    @Test
    void getByUserId_whenZeroSizeInParameter_thenBadRequestReturned() {
        long userId = 1L;
        long from = 0;
        int size = 0;
        String errorMessage = "must be greater than 0";

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(itemClient, never()).getByUserId(userId, from, size);
    }

    @SneakyThrows
    @Test
    void getById_whenValidRequest_thenItemReturned() {
        long userId = 1L;
        long itemId = 1L;

        when(itemClient.getById(userId, itemId))
                .thenReturn(ResponseEntity.ok(itemResponse));

        mockMvc.perform(get("/items/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(itemResponse.getId()), Long.class))
                .andExpect(jsonPath("$.name", equalTo(itemResponse.getName())))
                .andExpect(jsonPath("$.description", equalTo(itemResponse.getDescription())))
                .andExpect(jsonPath("$.available", equalTo(itemResponse.getAvailable())))
                .andExpect(jsonPath("$.comments").hasJsonPath())
                .andExpect(jsonPath("$.comments[0].id", equalTo(commentResponse.getId()), Long.class))
                .andExpect(jsonPath("$.comments[0].authorName", equalTo(commentResponse.getAuthorName())))
                .andExpect(jsonPath("$.comments[0].text", equalTo(commentResponse.getText())))
                .andExpect(jsonPath("$.comments[0].created",
                        equalTo(commentResponse.getCreated().format(dateTimeFormatter))))
                .andExpect(jsonPath("$.requestId", equalTo(itemResponse.getRequestId()), Long.class))
                .andExpect(jsonPath("$.lastBooking.id",
                        equalTo(itemResponse.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$.lastBooking.bookerId",
                        equalTo(itemResponse.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$.nextBooking", equalTo(itemResponse.getNextBooking())));

        verify(itemClient).getById(userId, itemId);
    }

    @SneakyThrows
    @Test
    void getById_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = 1L;
        long itemId = 1L;
        String errorMessage =
                "Required request header 'X-Sharer-User-Id' for method parameter type long is not present";

        mockMvc.perform(get("/items/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(errorMessage)));

        verify(itemClient, never()).getById(userId, itemId);
    }

    @SneakyThrows
    @Test
    void create_whenValidRequest_thenCreateItemReturned() {
        long userId = 1L;

        when(itemClient.create(userId, itemCreateRequest))
                .thenReturn(ResponseEntity.ok(itemResponse));

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(itemResponse.getId()), Long.class))
                .andExpect(jsonPath("$.name", equalTo(itemResponse.getName())))
                .andExpect(jsonPath("$.description", equalTo(itemResponse.getDescription())))
                .andExpect(jsonPath("$.available", equalTo(itemResponse.getAvailable())))
                .andExpect(jsonPath("$.comments").hasJsonPath())
                .andExpect(jsonPath("$.comments[0].id", equalTo(commentResponse.getId()), Long.class))
                .andExpect(jsonPath("$.comments[0].authorName", equalTo(commentResponse.getAuthorName())))
                .andExpect(jsonPath("$.comments[0].text", equalTo(commentResponse.getText())))
                .andExpect(jsonPath("$.comments[0].created",
                        equalTo(commentResponse.getCreated().format(dateTimeFormatter))))
                .andExpect(jsonPath("$.requestId", equalTo(itemResponse.getRequestId()), Long.class))
                .andExpect(jsonPath("$.lastBooking.id",
                        equalTo(itemResponse.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$.lastBooking.bookerId",
                        equalTo(itemResponse.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$.nextBooking", equalTo(itemResponse.getNextBooking())));

        verify(itemClient).create(userId, itemCreateRequest);
    }

    @SneakyThrows
    @Test
    void create_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage =
                "Required request header 'X-Sharer-User-Id' for method parameter type long is not present";

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(itemCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(errorMessage)));

        verify(itemClient, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenNameIsNull_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage = "Наименование не может быть пустым.";
        itemCreateRequest.setName(null);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", equalTo(errorMessage)));

        verify(itemClient, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenNameIsBlank_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage = "Наименование не может быть пустым.";
        itemCreateRequest.setName(" ");

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", equalTo(errorMessage)));

        verify(itemClient, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenDescriptionIsNull_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage = "Описание не может быть пустым.";
        itemCreateRequest.setDescription(null);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", equalTo(errorMessage)));

        verify(itemClient, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenDescriptionIsBlank_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage = "Описание не может быть пустым.";
        itemCreateRequest.setDescription(" ");

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", equalTo(errorMessage)));

        verify(itemClient, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenAvailableIsNull_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage = "Признак доступности вещи не может быть пустым.";
        itemCreateRequest.setAvailable(null);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", equalTo(errorMessage)));

        verify(itemClient, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void update_whenValidRequest_thenUpdatedItemReturned() {
        long userId = 1L;
        long itemId = 1L;

        when(itemClient.update(userId, itemId, itemUpdateRequest))
                .thenReturn(ResponseEntity.ok(itemResponse));

        mockMvc.perform(patch("/items/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(itemResponse.getId()), Long.class));

        verify(itemClient).update(userId, itemId, itemUpdateRequest);
    }

    @SneakyThrows
    @Test
    void update_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = 1L;
        long itemId = 1L;
        String errorMessage =
                "Required request header 'X-Sharer-User-Id' for method parameter type long is not present";

        mockMvc.perform(patch("/items/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(itemUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(errorMessage)));

        verify(itemClient, never()).update(anyLong(), anyLong(), any());
    }

    @SneakyThrows
    @Test
    void search_whenValidRequest_thenListOfItemReturned() {
        long userId = 1L;
        long from = 0;
        int size = 50;
        String substring = "text";

        when(itemClient.search(userId, substring, from, size))
                .thenReturn(ResponseEntity.ok(List.of(itemResponse)));

        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("text", substring)
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(itemResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", equalTo(itemResponse.getName())))
                .andExpect(jsonPath("$[0].description", equalTo(itemResponse.getDescription())))
                .andExpect(jsonPath("$[0].available", equalTo(itemResponse.getAvailable())));

        verify(itemClient).search(userId, substring, from, size);
    }

    @SneakyThrows
    @Test
    void search_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = 1L;
        long from = 0;
        int size = 50;
        String substring = "text";
        String errorMessage =
                "Required request header 'X-Sharer-User-Id' for method parameter type long is not present";

        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("text", substring)
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(errorMessage)));

        verify(itemClient, never()).search(userId, substring, from, size);
    }

    @SneakyThrows
    @Test
    void search_whenNegativeFromInParameter_thenBadRequestReturned() {
        long userId = 1L;
        long from = -1;
        int size = 50;
        String substring = "text";
        String errorMessage = "must be greater than or equal to 0";

        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("text", substring)
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(itemClient, never()).search(userId, substring, from, size);
    }

    @SneakyThrows
    @Test
    void search_whenZeroSizeInParameter_thenBadRequestReturned() {
        long userId = 1L;
        long from = 1;
        int size = 0;
        String substring = "text";
        String errorMessage = "must be greater than 0";

        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("text", substring)
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(itemClient, never()).search(userId, substring, from, size);
    }

    @SneakyThrows
    @Test
    void addComment_whenValidRequest_thenListOfItemReturned() {
        long userId = 1L;
        long itemId = 1L;

        when(itemClient.addComment(userId, itemId, commentCreateRequest))
                .thenReturn(ResponseEntity.ok(commentResponse));

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(commentCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(commentResponse.getId()), Long.class))
                .andExpect(jsonPath("$.authorName", equalTo(commentResponse.getAuthorName())))
                .andExpect(jsonPath("$.text", equalTo(commentResponse.getText())))
                .andExpect(jsonPath("$.created",
                        equalTo(commentResponse.getCreated().format(dateTimeFormatter))));

        verify(itemClient).addComment(userId, itemId, commentCreateRequest);
    }

    @SneakyThrows
    @Test
    void addComment_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = 1L;
        long itemId = 1L;
        String errorMessage =
                "Required request header 'X-Sharer-User-Id' for method parameter type long is not present";

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(commentCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(errorMessage)));

        verify(itemClient, never()).addComment(anyLong(), anyLong(), any());
    }

    @SneakyThrows
    @Test
    void addComment_whenTextIsNull_thenBadRequestReturned() {
        long userId = 1L;
        long itemId = 1L;
        String errorMessage = "Комментарий не может быть пустым.";
        commentCreateRequest.setText(null);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(commentCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(itemClient, never()).addComment(anyLong(), anyLong(), any());
    }

    @SneakyThrows
    @Test
    void addComment_whenTextIsBlank_thenBadRequestReturned() {
        long userId = 1L;
        long itemId = 1L;
        String errorMessage = "Комментарий не может быть пустым.";
        commentCreateRequest.setText(" ");

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(commentCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));
        verify(itemClient, never()).addComment(anyLong(), anyLong(), any());
    }
}