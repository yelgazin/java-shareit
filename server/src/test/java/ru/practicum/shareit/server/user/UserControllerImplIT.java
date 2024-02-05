package ru.practicum.shareit.server.user;

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
import ru.practicum.shareit.server.common.exception.EntityNotFoundException;
import ru.practicum.shareit.server.helper.ObjectGenerator;
import ru.practicum.shareit.server.user.controller.UserControllerImpl;
import ru.practicum.shareit.server.user.dto.UserCreateRequest;
import ru.practicum.shareit.server.user.dto.UserUpdateRequest;
import ru.practicum.shareit.server.user.entity.User;
import ru.practicum.shareit.server.user.mapper.UserConverter;
import ru.practicum.shareit.server.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UserControllerImpl.class, UserConverter.class, ObjectGenerator.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerImplIT {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ObjectGenerator objectGenerator;

    @MockBean
    private UserService userService;

    private UserCreateRequest userCreateRequest1;
    private UserUpdateRequest userUpdateRequest1;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = objectGenerator.next(User.class);
        user2 = objectGenerator.next(User.class);

        user1.setId(1L);
        user2.setId(2L);

        userCreateRequest1 = objectGenerator.next(UserCreateRequest.class);
        userUpdateRequest1 = objectGenerator.next(UserUpdateRequest.class);
    }

    @SneakyThrows
    @Test
    void getAll_whenUsersExist_thenListOfTwoUsersReturned() {
        when(userService.getAll())
                .thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(user1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(user1.getName())))
                .andExpect(jsonPath("$[0].email", is(user1.getEmail())))
                .andExpect(jsonPath("$[1].id", is(user2.getId()), Long.class))
                .andExpect(jsonPath("$[1].name", is(user2.getName())))
                .andExpect(jsonPath("$[1].email", is(user2.getEmail()))
                );

        verify(userService).getAll();
    }

    @SneakyThrows
    @Test
    void getById_whenUserExists_thenUserReturned() {
        long userId = 1L;
        when(userService.getById(userId))
                .thenReturn(user1);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user1.getName())))
                .andExpect(jsonPath("$.email", is(user1.getEmail())));

        verify(userService).getById(userId);
    }

    @SneakyThrows
    @Test
    void getById_whenUserNotExists_thenNotFoundReturned() {
        long userId = 0L;
        String errorMessage = "Объект не найден.";

        when(userService.getById(userId))
                .thenThrow(new EntityNotFoundException(errorMessage));

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", Matchers.containsString(errorMessage)));

        verify(userService).getById(userId);
    }

    @SneakyThrows
    @Test
    void create_whenValidUserCreateRequest_thenCreatedUserReturned() {
        long newUserId = 1L;
        when(userService.create(any()))
                .thenAnswer(invocationOnMock -> {
                    User createdUser = invocationOnMock.getArgument(0, User.class);
                    createdUser.setId(newUserId);
                    return createdUser;
                });

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(userCreateRequest1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(newUserId), Long.class))
                .andExpect(jsonPath("$.name", is(userCreateRequest1.getName())))
                .andExpect(jsonPath("$.email", is(userCreateRequest1.getEmail())));

        verify(userService).create(any());
    }

    @SneakyThrows
    @Test
    void update_whenValidUserUpdateRequest_thenOkReturned() {
        long userId = 1L;

        mockMvc.perform(patch("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(userUpdateRequest1)))
                .andExpect(status().isOk());

        verify(userService).update(anyLong(), any());
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

        verify(userService).delete(userId);
    }
}