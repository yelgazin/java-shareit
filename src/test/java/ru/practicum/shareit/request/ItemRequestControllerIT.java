package ru.practicum.shareit.request;

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
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.helper.ObjectGenerator;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.dto.ItemRequestConverter;
import ru.practicum.shareit.request.dto.ItemRequestCreateRequest;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ItemRequestController.class, ItemRequestConverter.class, ObjectGenerator.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestControllerIT {

    private final ObjectMapper objectMapper;
    private final ObjectGenerator objectGenerator;
    private final MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    private ItemRequestCreateRequest itemRequestCreateRequest;
    private ItemRequest itemRequest;
    private User author;

    private User owner;
    private Item item;

    @BeforeEach
    void setUp() {
        itemRequestCreateRequest = objectGenerator.next(ItemRequestCreateRequest.class);
        itemRequest = objectGenerator.next(ItemRequest.class);
        item = objectGenerator.next(Item.class);
        author = objectGenerator.next(User.class);
        owner = objectGenerator.next(User.class);

        owner.setId(1L);
        author.setId(2L);

        item.setId(1L);

        itemRequest.setId(1L);
        itemRequest.setAuthor(author);
        itemRequest.setItems(Set.of(item));
    }

    @SneakyThrows
    @Test
    void create_whenValidItermRequestCreateRequest_thenItemRequestResponseReturned() {
        long userId = 1L;

        when(itemRequestService.create(anyLong(), any()))
                .thenReturn(itemRequest);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemRequestCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", equalTo(itemRequestCreateRequest.getDescription())));

        verify(itemRequestService).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage =
                "Required request header 'X-Sharer-User-Id' for method parameter type long is not present";

        when(itemRequestService.create(anyLong(), any()))
                .thenReturn(itemRequest);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(itemRequestCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(errorMessage)));

        verify(itemRequestService, never()).create(anyLong(), any());
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

        verify(itemRequestService, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenDescriptionIsBlank_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage = "Описание не может быть пустым.";
        itemRequestCreateRequest.setDescription(" ");

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemRequestCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.equalTo(errorMessage)));

        verify(itemRequestService, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void getAllMyRequests_whenValidRequest_thenListOfItemRequestReturned() {
        long userId = 1L;

        when(itemRequestService.getAllMyRequests(userId))
                .thenReturn(List.of(itemRequest));

        mockMvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", equalTo(itemRequestCreateRequest.getDescription())));

        verify(itemRequestService).getAllMyRequests(userId);
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

        verify(itemRequestService, never()).getAllMyRequests(userId);
    }

    @SneakyThrows
    @Test
    void getAllNotMyRequests_whenValidRequest_thenListOfItemRequestReturned() {
        long userId = 1L;

        when(itemRequestService.getAllNotUserRequests(anyLong(), anyLong(), anyInt()))
                .thenReturn(List.of(itemRequest));

        mockMvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", equalTo(itemRequestCreateRequest.getDescription())));

        verify(itemRequestService).getAllNotUserRequests(anyLong(), anyLong(), anyInt());
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

        verify(itemRequestService, never()).getAllNotUserRequests(anyLong(), anyLong(), anyInt());
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

        verify(itemRequestService, never()).getAllNotUserRequests(anyLong(), anyLong(), anyInt());
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

        verify(itemRequestService, never()).getAllNotUserRequests(anyLong(), anyLong(), anyInt());
    }

    @SneakyThrows
    @Test
    void getByRequestId_whenValidRequest_thenItemRequestReturned() {
        long userId = 1L;
        long itemRequestId = 1L;

        when(itemRequestService.getByRequestId(userId, itemRequestId))
                .thenReturn(itemRequest);

        mockMvc.perform(get("/requests/{requestId}", itemRequestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", equalTo(itemRequestCreateRequest.getDescription())));

        verify(itemRequestService).getByRequestId(userId, itemRequestId);
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

        verify(itemRequestService, never()).getByRequestId(userId, itemRequestId);
    }
}