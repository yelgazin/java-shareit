package ru.practicum.shareit.gateway.user.controller;

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
import ru.practicum.shareit.gateway.user.client.UserClient;
import ru.practicum.shareit.server.user.dto.UserCreateRequest;
import ru.practicum.shareit.server.user.dto.UserResponse;
import ru.practicum.shareit.server.user.dto.UserUpdateRequest;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UserControllerImpl.class, ObjectGenerator.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerImplIT {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ObjectGenerator objectGenerator;

    @MockBean
    private UserClient userClient;

    private UserCreateRequest userCreateRequest;
    private UserUpdateRequest userUpdateRequest;

    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userCreateRequest = objectGenerator.next(UserCreateRequest.class);
        userUpdateRequest = objectGenerator.next(UserUpdateRequest.class);

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setName(userCreateRequest.getName());
        userResponse.setEmail(userCreateRequest.getEmail());
    }

    @SneakyThrows
    @Test
    void getAll_whenUsersExist_thenListOfUsersReturned() {

        when(userClient.getAll())
                .thenReturn(ResponseEntity.ok(List.of(userResponse)));

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(userResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(userResponse.getName())))
                .andExpect(jsonPath("$[0].email", is(userResponse.getEmail()))
                );

        verify(userClient).getAll();
    }

    @SneakyThrows
    @Test
    void getById_whenUserExists_thenUserReturned() {
        long userId = userResponse.getId();

        when(userClient.getById(userId))
                .thenReturn(ResponseEntity.ok(userResponse));

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userResponse.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userResponse.getName())))
                .andExpect(jsonPath("$.email", is(userResponse.getEmail())));

        verify(userClient).getById(userId);
    }

    @SneakyThrows
    @Test
    void create_whenValidUserCreateRequest_thenCreatedUserReturned() {
        long newUserId = 1L;

        when(userClient.create(any()))
                .thenReturn(ResponseEntity.ok(userResponse));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(userResponse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userResponse.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userResponse.getName())))
                .andExpect(jsonPath("$.email", is(userResponse.getEmail())));

        verify(userClient).create(any());
    }

    @SneakyThrows
    @Test
    void create_whenUserNameIsNull_thenBadRequestReturned() {
        String errorMessage = "Имя пользователя не может пустым.";
        userCreateRequest.setName(null);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(userClient, never()).create(any());
    }

    @SneakyThrows
    @Test
    void create_whenUserNameIsBlank_thenBadRequestReturned() {
        String errorMessage = "Имя пользователя не может пустым.";
        userCreateRequest.setName("");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(userClient, never()).create(any());
    }

    @SneakyThrows
    @Test
    void create_whenEmailIsNull_thenBadRequestReturned() {
        String errorMessage = "Электронная почта не может быть пустой.";
        userCreateRequest.setEmail(null);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(userClient, never()).create(any());
    }

    @SneakyThrows
    @Test
    void create_whenEmailIsBlank_thenBadRequestReturned() {
        String errorMessage = "Электронная почта не может быть пустой.";
        userCreateRequest.setEmail("");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(userClient, never()).create(any());
    }

    @SneakyThrows
    @Test
    void create_whenEmailInvalid_thenBadRequestReturned() {
        String errorMessage = "Электронная почта не соответствует формату \"user@mail.ru\".";
        userCreateRequest.setEmail("incorrect email format@");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(userClient, never()).create(any());
    }

    @SneakyThrows
    @Test
    void update_whenValidUserUpdateRequest_thenOkReturned() {
        long userId = 1L;

        mockMvc.perform(patch("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(userUpdateRequest)))
                .andExpect(status().isOk());

        verify(userClient).update(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void update_whenInvalidEmail_thenBadRequestReturned() {
        String errorMessage = "Электронная почта не соответствует формату \"user@mail.ru\".";
        long userId = 1L;
        userUpdateRequest.setEmail("invalid email format@.com");

        mockMvc.perform(patch("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(userUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(userClient, never()).update(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void delete_whenUserExistsOrNot_thenOkReturned() {
        long userId = 1L;

        mockMvc.perform(delete("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        verify(userClient).delete(userId);
    }
}