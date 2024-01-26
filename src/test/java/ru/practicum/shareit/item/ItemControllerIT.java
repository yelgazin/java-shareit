package ru.practicum.shareit.item;

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
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.helper.ObjectGenerator;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.exception.ItemException;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ItemController.class, ItemConverter.class, CommentConverter.class, ObjectGenerator.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemControllerIT {

    private final ObjectMapper objectMapper;
    private final ObjectGenerator objectGenerator;
    private final MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private ItemCreateRequest itemCreateRequest;
    private ItemUpdateRequest itemUpdateRequest;
    private CommentCreateRequest commentCreateRequest;

    private User owner;
    private User booker;
    private Item item;
    private Comment comment;
    private Booking booking;

    @BeforeEach
    void setUp() {
        itemCreateRequest = objectGenerator.next(ItemCreateRequest.class);
        itemUpdateRequest = objectGenerator.next(ItemUpdateRequest.class);
        commentCreateRequest = objectGenerator.next(CommentCreateRequest.class);

        owner = objectGenerator.next(User.class);
        booker = objectGenerator.next(User.class);
        item = objectGenerator.next(Item.class);
        comment = objectGenerator.next(Comment.class);
        booking = objectGenerator.next(Booking.class);

        owner.setId(1L);
        booker.setId(2L);
        item.setId(1L);
        item.setOwner(owner);
        comment.setId(1L);
        comment.setItem(item);
        comment.setAuthor(booker);
        booking.setId(1L);
        booking.setBooker(booker);
        item.setLastBooking(booking);
        item.setComments(Set.of(comment));
    }

    @SneakyThrows
    @Test
    void getByUserId_whenValidRequest_thenListOfItemReturned() {
        long userId = owner.getId();
        long from = 0;
        int size = 50;

        when(itemService.getByUserId(userId, from, size))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", equalTo(item.getName())))
                .andExpect(jsonPath("$[0].description", equalTo(item.getDescription())))
                .andExpect(jsonPath("$[0].available", equalTo(item.getAvailable())));

        verify(itemService).getByUserId(userId, from, size);
    }

    @SneakyThrows
    @Test
    void getByUserId_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = owner.getId();
        long from = 0;
        int size = 50;
        String errorMessage =
                "Required request header 'X-Sharer-User-Id' for method parameter type long is not present";

        when(itemService.getByUserId(userId, from, size))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(errorMessage)));

        verify(itemService, never()).getByUserId(userId, from, size);
    }

    @SneakyThrows
    @Test
    void getByUserId_whenNegativeFromInParameter_thenBadRequestReturned() {
        long userId = owner.getId();
        long from = -1;
        int size = 50;
        String errorMessage = "must be greater than or equal to 0";

        when(itemService.getByUserId(userId, from, size))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(itemService, never()).getByUserId(userId, from, size);
    }

    @SneakyThrows
    @Test
    void getByUserId_whenZeroSizeInParameter_thenBadRequestReturned() {
        long userId = owner.getId();
        long from = 0;
        int size = 0;
        String errorMessage = "must be greater than 0";

        when(itemService.getByUserId(userId, from, size))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(itemService, never()).getByUserId(userId, from, size);
    }

    @SneakyThrows
    @Test
    void getById_whenValidRequest_thenItemReturned() {
        long userId = owner.getId();
        long itemId = item.getId();

        when(itemService.getById(itemId))
                .thenReturn(item);

        mockMvc.perform(get("/items/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", equalTo(item.getName())))
                .andExpect(jsonPath("$.description", equalTo(item.getDescription())))
                .andExpect(jsonPath("$.available", equalTo(item.getAvailable())));

        verify(itemService).getById(itemId);
    }

    @SneakyThrows
    @Test
    void getById_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = owner.getId();
        long itemId = item.getId();
        String errorMessage =
                "Required request header 'X-Sharer-User-Id' for method parameter type long is not present";

        when(itemService.getById(itemId))
                .thenReturn(item);

        mockMvc.perform(get("/items/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(errorMessage)));

        verify(itemService, never()).getById(itemId);
    }

    @SneakyThrows
    @Test
    void create_whenValidRequest_thenCreateItemReturned() {
        long userId = 1L;

        when(itemService.create(anyLong(), any()))
                .thenReturn(item);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", equalTo(item.getName())))
                .andExpect(jsonPath("$.description", equalTo(item.getDescription())))
                .andExpect(jsonPath("$.available", equalTo(item.getAvailable())));

        verify(itemService).create(anyLong(), any());
    }


    @SneakyThrows
    @Test
    void create_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = 1L;
        String errorMessage =
                "Required request header 'X-Sharer-User-Id' for method parameter type long is not present";

        when(itemService.create(anyLong(), any()))
                .thenReturn(item);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(itemCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(errorMessage)));

        verify(itemService, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenNameIsNull_thenBadRequestReturned() {
        long userId = 1L;
        //String errorMessage = "Имя не может быть пустым.";
        itemCreateRequest.setName(null);

        when(itemService.create(anyLong(), any()))
                .thenReturn(item);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemCreateRequest)))
                .andExpect(status().isBadRequest()
                        //.andExpect(status().reason(containsString(errorMessage))
                );

        verify(itemService, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenNameIsBlank_thenBadRequestReturned() {
        long userId = 1L;
        //String errorMessage = "Имя не может быть пустым.";
        itemCreateRequest.setName(" ");

        when(itemService.create(anyLong(), any()))
                .thenReturn(item);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemCreateRequest)))
                .andExpect(status().isBadRequest()
                        //.andExpect(status().reason(containsString(errorMessage))
                );

        verify(itemService, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenDescriptionIsNull_thenBadRequestReturned() {
        long userId = 1L;
        //String errorMessage = "Описание не может быть пустым.";
        itemCreateRequest.setDescription(null);

        when(itemService.create(anyLong(), any()))
                .thenReturn(item);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemCreateRequest)))
                .andExpect(status().isBadRequest()
                        //.andExpect(status().reason(containsString(errorMessage))
                );

        verify(itemService, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenDescriptionIsBlank_thenBadRequestReturned() {
        long userId = 1L;
        //String errorMessage = "Описание не может быть пустым.";
        itemCreateRequest.setDescription(" ");

        when(itemService.create(anyLong(), any()))
                .thenReturn(item);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemCreateRequest)))
                .andExpect(status().isBadRequest()
                        //.andExpect(status().reason(containsString(errorMessage))
                );

        verify(itemService, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void create_whenAvailableIsNull_thenBadRequestReturned() {
        long userId = 1L;
        //String errorMessage = "Признак доступности вещи не может быть пустым..";
        itemCreateRequest.setAvailable(null);

        when(itemService.create(anyLong(), any()))
                .thenReturn(item);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemCreateRequest)))
                .andExpect(status().isBadRequest()
                        //.andExpect(status().reason(containsString(errorMessage))
                );

        verify(itemService, never()).create(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void update_whenValidRequest_thenUpdatedItemReturned() {
        long userId = 1L;
        long itemId = 1L;

        when(itemService.update(anyLong(), anyLong(), any()))
                .thenReturn(item);

        mockMvc.perform(patch("/items/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", equalTo(item.getName())))
                .andExpect(jsonPath("$.description", equalTo(item.getDescription())))
                .andExpect(jsonPath("$.available", equalTo(item.getAvailable())));

        verify(itemService).update(anyLong(), anyLong(), any());
    }

    @SneakyThrows
    @Test
    void update_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = 1L;
        long itemId = 1L;
        String errorMessage =
                "Required request header 'X-Sharer-User-Id' for method parameter type long is not present";

        when(itemService.update(anyLong(), anyLong(), any()))
                .thenReturn(item);

        mockMvc.perform(patch("/items/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(itemUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(errorMessage)));

        verify(itemService, never()).update(anyLong(), anyLong(), any());
    }

    @SneakyThrows
    @Test
    void search_whenValidRequest_thenListOfItemReturned() {
        long userId = 1L;
        long from = 0;
        int size = 50;
        String substring = "text";

        when(itemService.findAvailableBySubstring(substring, from, size))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .param("text", substring)
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", equalTo(item.getName())))
                .andExpect(jsonPath("$[0].description", equalTo(item.getDescription())))
                .andExpect(jsonPath("$[0].available", equalTo(item.getAvailable())));

        verify(itemService).findAvailableBySubstring(substring, from, size);
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

        when(itemService.findAvailableBySubstring(substring, from, size))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("text", substring)
                        .param("from", Long.toString(from))
                        .param("size", Integer.toString(size)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(errorMessage)));

        verify(itemService, never()).findAvailableBySubstring(substring, from, size);
    }

    @SneakyThrows
    @Test
    void search_whenNegativeFromInParameter_thenBadRequestReturned() {
        long userId = 1L;
        long from = -1;
        int size = 50;
        String substring = "text";
        String errorMessage = "must be greater than or equal to 0";

        when(itemService.findAvailableBySubstring(substring, from, size))
                .thenReturn(List.of(item));

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

        verify(itemService, never()).findAvailableBySubstring(substring, from, size);
    }

    @SneakyThrows
    @Test
    void search_whenZeroSizeInParameter_thenBadRequestReturned() {
        long userId = 1L;
        long from = 1;
        int size = 0;
        String substring = "text";
        String errorMessage = "must be greater than 0";

        when(itemService.findAvailableBySubstring(substring, from, size))
                .thenReturn(List.of(item));

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

        verify(itemService, never()).findAvailableBySubstring(substring, from, size);
    }

    @SneakyThrows
    @Test
    void addComment_whenValidRequest_thenListOfItemReturned() {
        long userId = 1L;
        long itemId = 1L;

        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(comment);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(commentCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(comment.getId()), Long.class))
                .andExpect(jsonPath("$.authorName", equalTo(comment.getAuthor().getName())))
                .andExpect(jsonPath("$.text", equalTo(comment.getText())))
                .andExpect(jsonPath("$.created", equalTo(comment.getCreated())));

        verify(itemService).addComment(anyLong(), anyLong(), any());
    }

    @SneakyThrows
    @Test
    void addComment_whenMissingUserIdInHeader_thenBadRequestReturned() {
        long userId = 1L;
        long itemId = 1L;
        String errorMessage =
                "Required request header 'X-Sharer-User-Id' for method parameter type long is not present";

        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(comment);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(commentCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(errorMessage)));

        verify(itemService, never()).addComment(anyLong(), anyLong(), any());
    }

    @SneakyThrows
    @Test
    void addComment_whenTextIsNull_thenBadRequestReturned() {
        long userId = 1L;
        long itemId = 1L;
        String errorMessage = "Комментарий не может быть пустым.";
        commentCreateRequest.setText(null);

        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(comment);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(commentCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(itemService, never()).addComment(anyLong(), anyLong(), any());
    }

    @SneakyThrows
    @Test
    void addComment_whenTextIsBlank_thenBadRequestReturned() {
        long userId = 1L;
        long itemId = 1L;
        String errorMessage = "Комментарий не может быть пустым.";
        commentCreateRequest.setText(" ");

        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(comment);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(commentCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));
        verify(itemService, never()).addComment(anyLong(), anyLong(), any());
    }

    // TODO: Вынести в отдельный класс проверки.
    @SneakyThrows
    @Test
    void addComment_whenItemNotExists_thenListOfItemReturned() {
        long userId = 1L;
        long itemId = 1L;

        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenThrow(ItemException.class);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(commentCreateRequest)))
                .andExpect(status().isBadRequest());

        verify(itemService).addComment(anyLong(), anyLong(), any());
    }
}