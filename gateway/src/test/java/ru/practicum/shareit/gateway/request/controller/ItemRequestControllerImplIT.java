package ru.practicum.shareit.gateway.request.controller;

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
import ru.practicum.shareit.gateway.request.client.ItemRequestClient;
import ru.practicum.shareit.server.item.dto.ItemCreateRequest;
import ru.practicum.shareit.server.request.dto.ItemRequestCreateRequest;
import ru.practicum.shareit.server.request.dto.ItemRequestResponse;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ItemRequestControllerImpl.class, ObjectGenerator.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestControllerImplIT {

    private final ObjectMapper objectMapper;
    private final ObjectGenerator objectGenerator;
    private final MockMvc mockMvc;

    @MockBean
    private ItemRequestClient itemRequestClient;

    private ItemRequestCreateRequest itemRequestCreateRequest;
    private ItemRequestResponse itemRequestResponse;
    private ItemRequestResponse.ItemView itemView;
    private ItemCreateRequest itemCreateRequest;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @BeforeEach
    void setUp() {
        itemRequestCreateRequest = objectGenerator.next(ItemRequestCreateRequest.class);
        itemCreateRequest = objectGenerator.next(ItemCreateRequest.class);

        itemView = new ItemRequestResponse.ItemView();
        itemView.setId(1L);
        itemView.setName(itemCreateRequest.getName());
        itemView.setDescription(itemCreateRequest.getDescription());
        itemView.setAvailable(itemCreateRequest.getAvailable());
        itemView.setRequestId(1L);

        itemRequestResponse = new ItemRequestResponse();
        itemRequestResponse.setId(1L);
        itemRequestResponse.setCreated(LocalDateTime.of(2021, 12, 23, 23, 59, 0));
        itemRequestResponse.setDescription(itemRequestCreateRequest.getDescription());
        itemRequestResponse.setItems(Set.of(itemView));
    }

    @SneakyThrows
    @Test
    void create_whenValidItermRequestCreateRequest_thenItemRequestResponseReturned() {
        long userId = 1L;

        when(itemRequestClient.create(anyLong(), any()))
                .thenReturn(ResponseEntity.ok(itemRequestResponse));

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemRequestCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(itemRequestResponse.getId()), Long.class))
                .andExpect(jsonPath("$.description", equalTo(itemRequestResponse.getDescription())))
                .andExpect(jsonPath("$.created", equalTo(itemRequestResponse.getCreated().format(dateTimeFormatter))))
                .andExpect(jsonPath("$.items[0].id", equalTo(itemView.getId()), Long.class))
                .andExpect(jsonPath("$.items[0].name", equalTo(itemView.getName())))
                .andExpect(jsonPath("$.items[0].description", equalTo(itemView.getDescription())))
                .andExpect(jsonPath("$.items[0].requestId", equalTo(itemView.getRequestId()), Long.class))
                .andExpect(jsonPath("$.items[0].available", equalTo(itemView.getAvailable())));

        verify(itemRequestClient).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage =
                "Required request header 'X-Sharer-User-Id' for method parameter type long is not present";

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(itemRequestCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(errorMessage)));

        verify(itemRequestClient, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenDescriptionIsNull_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage = "Описание не может быть пустым.";
        itemRequestCreateRequest.setDescription(null);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemRequestCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.equalTo(errorMessage)));

        verify(itemRequestClient, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenDescriptionIsBlank_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage = "Описание не может быть пустым.";
        itemRequestCreateRequest.setDescription("");

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemRequestCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.equalTo(errorMessage)));

        verify(itemRequestClient, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void getAllMyRequests_whenValidRequest_thenListOfItemRequestReturned() {
        long userId = 1L;

        when(itemRequestClient.getAllMyRequests(userId))
                .thenReturn(ResponseEntity.ok(List.of(itemRequestResponse)));

        mockMvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(itemRequestResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", equalTo(itemRequestCreateRequest.getDescription())));

        verify(itemRequestClient).getAllMyRequests(userId);
    }

    @SneakyThrows
    @Test
    void getAllMyRequests_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage =
                "Required request header 'X-Sharer-User-Id' for method parameter type long is not present";

        mockMvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(errorMessage)));

        verify(itemRequestClient, never()).getAllMyRequests(userId);
    }

    @SneakyThrows
    @Test
    void getAllNotMyRequests_whenValidRequest_thenListOfItemRequestReturned() {
        long userId = 1L;

        when(itemRequestClient.getAllNotMyRequests(anyLong(), anyLong(), anyInt()))
                .thenReturn(ResponseEntity.ok(List.of(itemRequestResponse)));

        mockMvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(itemRequestResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", equalTo(itemRequestCreateRequest.getDescription())));

        verify(itemRequestClient).getAllNotMyRequests(anyLong(), anyLong(), anyInt());
    }

    @SneakyThrows
    @Test
    void getAllNotMyRequests_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage =
                "Required request header 'X-Sharer-User-Id' for method parameter type long is not present";

        mockMvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(errorMessage)));

        verify(itemRequestClient, never()).getAllNotMyRequests(anyLong(), anyLong(), anyInt());
    }

    @SneakyThrows
    @Test
    void getAllNotMyRequests_whenNegativeFromInParameter_thenBadRequestReturned() {
        long userId = 1L;
        long from = -1;
        int size = 50;
        String errorMessage = "must be greater than or equal to 0";

        mockMvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(itemRequestClient, never()).getAllNotMyRequests(anyLong(), anyLong(), anyInt());
    }

    @SneakyThrows
    @Test
    void getAllNotMyRequests_whenZeroSizeInParameter_thenBadRequestReturned() {
        long userId = 1L;
        long from = 10;
        int size = 0;
        String errorMessage = "must be greater than 0";

        mockMvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(itemRequestClient, never()).getAllNotMyRequests(anyLong(), anyLong(), anyInt());
    }

    @SneakyThrows
    @Test
    void getByRequestId_whenValidRequest_thenItemRequestReturned() {
        long userId = 1L;
        long itemRequestId = 1L;

        when(itemRequestClient.getByRequestId(userId, itemRequestId))
                .thenReturn(ResponseEntity.ok(itemRequestResponse));

        mockMvc.perform(get("/requests/{requestId}", itemRequestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(itemRequestResponse.getId()), Long.class))
                .andExpect(jsonPath("$.description", equalTo(itemRequestCreateRequest.getDescription())));

        verify(itemRequestClient).getByRequestId(userId, itemRequestId);
    }

    @SneakyThrows
    @Test
    void getByRequestId_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = 1L;
        long itemRequestId = 1L;
        String errorMessage =
                "Required request header 'X-Sharer-User-Id' for method parameter type long is not present";

        mockMvc.perform(get("/requests/{requestId}", itemRequestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(errorMessage)));

        verify(itemRequestClient, never()).getByRequestId(userId, itemRequestId);
    }
}