package ru.practicum.shareit.gateway.item.client;

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
import ru.practicum.shareit.gateway.common.exception.RestException;
import ru.practicum.shareit.server.item.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemClientTest extends AbstractClientTest {

    private ItemClient itemClient;

    private ItemCreateRequest itemCreateRequest;
    private ItemUpdateRequest itemUpdateRequest;
    private CommentCreateRequest commentCreateRequest;
    private ItemResponse itemResponse;
    private CommentResponse commentResponse;
    private ItemResponse.BookingView lastBookingView;

    @BeforeEach
    void setUp() {
        init();
        itemClient = new ItemClient("http://localhost", restTemplateBuilder);

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

    @Test
    void getByUserId_whenCall_thenResponseOkReturned() {
        long userId = 1L;
        long from = 0;
        int size = 50;

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(List.of(itemResponse));

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any(), ArgumentMatchers.<Map<String, ?>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = itemClient.getByUserId(userId, from, size);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void getById_whenCall_thenResponseOkReturned() {
        long userId = 1L;
        long bookingId = 1L;

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(itemResponse);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any(), ArgumentMatchers.<Map<String, ?>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = itemClient.getById(userId, bookingId);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void create_whenCallCreate_thenResponseOkReturned() {
        long userId = 1L;
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(itemResponse);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = itemClient.create(userId, itemCreateRequest);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void update_whenCall_thenResponseOkReturned() {
        long userId = 1L;
        long itemId = 1L;
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(itemResponse);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any(), ArgumentMatchers.<Map<String, ?>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = itemClient.update(userId, itemId, itemUpdateRequest);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void search_whenCall_thenResponseOkReturned() {
        long userId = 1L;
        long from = 0;
        int size = 50;
        String substring = "text";

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(List.of(itemResponse));

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any(), ArgumentMatchers.<Map<String, ?>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = itemClient.search(userId, substring, from, size);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void addComment_whenCall_thenResponseOkReturned() {
        long userId = 1L;
        long itemId = 1L;

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(commentResponse);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any(), ArgumentMatchers.<Map<String, ?>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = itemClient.addComment(userId, itemId, commentCreateRequest);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void addComment_whenCallWithInvalidParameters_thenResponseBadRequestReturned() {
        long userId = 1L;
        long itemId = 1L;

        ResponseEntity<Object> responseEntity =
                new ResponseEntity<>(new RestException("error", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any(), ArgumentMatchers.<Map<String, ?>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = itemClient.addComment(userId, itemId, commentCreateRequest);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }
}