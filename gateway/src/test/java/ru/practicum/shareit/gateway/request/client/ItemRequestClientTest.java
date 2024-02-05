package ru.practicum.shareit.gateway.request.client;

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
import ru.practicum.shareit.server.item.dto.ItemCreateRequest;
import ru.practicum.shareit.server.request.dto.ItemRequestCreateRequest;
import ru.practicum.shareit.server.request.dto.ItemRequestResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestClientTest extends AbstractClientTest {

    private ItemRequestClient itemRequestClient;

    private ItemRequestCreateRequest itemRequestCreateRequest;
    private ItemRequestResponse itemRequestResponse;
    private ItemRequestResponse.ItemView itemView;
    private ItemCreateRequest itemCreateRequest;

    @BeforeEach
    void setUp() {
        init();
        itemRequestClient = new ItemRequestClient("http://localhost", restTemplateBuilder);

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

    @Test
    void create_whenCall_thenResponseOkReturned() {
        long userId = 1L;

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(itemRequestResponse);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = itemRequestClient.create(userId, itemRequestCreateRequest);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void getAllMyRequests_whenCall_thenResponseOkReturned() {
        long userId = 1L;

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(List.of(itemRequestResponse));

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = itemRequestClient.getAllMyRequests(userId);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void getAllNotMyRequests_whenCall_thenResponseOkReturned() {
        long userId = 1L;
        long from = 0;
        int size = 50;

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(List.of(itemRequestResponse));

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any(), ArgumentMatchers.<Map<String, ?>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = itemRequestClient.getAllNotMyRequests(userId, from, size);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void getByRequestId_whenCall_thenResponseOkReturned() {
        long userId = 1L;
        long requestId = 1L;

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(itemRequestResponse);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any(), ArgumentMatchers.<Map<String, ?>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = itemRequestClient.getByRequestId(userId, requestId);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }
}