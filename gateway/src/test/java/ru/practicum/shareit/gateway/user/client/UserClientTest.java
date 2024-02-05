package ru.practicum.shareit.gateway.user.client;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.gateway.common.client.AbstractClientTest;
import ru.practicum.shareit.server.user.dto.UserCreateRequest;
import ru.practicum.shareit.server.user.dto.UserResponse;
import ru.practicum.shareit.server.user.dto.UserUpdateRequest;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserClientTest extends AbstractClientTest {

    private UserClient userClient;
    private UserCreateRequest userCreateRequest;
    private UserUpdateRequest userUpdateRequest;

    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        init();
        userClient = new UserClient("http://localhost", restTemplateBuilder);

        userCreateRequest = objectGenerator.next(UserCreateRequest.class);
        userUpdateRequest = objectGenerator.next(UserUpdateRequest.class);

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setName(userCreateRequest.getName());
        userResponse.setEmail(userCreateRequest.getEmail());
    }

    @Test
    void getAll_whenCall_thenResponseOkReturned() {
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(List.of(userResponse));

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = userClient.getAll();

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void getById_whenCall_thenResponseOkReturned() {
        long userId = 1L;

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(userResponse);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any(), ArgumentMatchers.<Map<String, ?>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = userClient.getById(userId);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void create_whenCall_thenResponseOkReturned() {
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(userResponse);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = userClient.create(userCreateRequest);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void update_whenCall_thenResponseOkReturned() {
        long userId = 1L;
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(userResponse);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any(), ArgumentMatchers.<Map<String, ?>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = userClient.update(userId, userUpdateRequest);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void delete_whenCall_thenResponseOkReturned() {
        long userId = 1L;
        ResponseEntity<Object> responseEntity = ResponseEntity.ok().build();

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any(), ArgumentMatchers.<Map<String, ?>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = userClient.delete(userId);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }
}