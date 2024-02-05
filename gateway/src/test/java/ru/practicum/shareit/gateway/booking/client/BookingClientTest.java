package ru.practicum.shareit.gateway.booking.client;

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
import ru.practicum.shareit.server.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.server.booking.dto.BookingResponse;
import ru.practicum.shareit.server.booking.entity.StateFilter;
import ru.practicum.shareit.server.booking.entity.Status;
import ru.practicum.shareit.server.item.dto.CommentCreateRequest;
import ru.practicum.shareit.server.item.dto.CommentResponse;
import ru.practicum.shareit.server.item.dto.ItemCreateRequest;
import ru.practicum.shareit.server.item.dto.ItemResponse;
import ru.practicum.shareit.server.user.dto.UserResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingClientTest extends AbstractClientTest {

    private BookingClient bookingClient;

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

        init();
        bookingClient = new BookingClient("http://localhost", restTemplateBuilder);

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

    @Test
    void create_whenCallCreate_thenResponseOkReturned() {
        long userId = 1L;
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(bookingResponse);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = bookingClient.create(userId, bookingCreateRequest);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void create_whenCallCreateWithInvalidParameters_thenResponseBadRequestReturned() {
        long userId = 1L;
        ResponseEntity<Object> responseEntity =
                new ResponseEntity<>(new RestException("error", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = bookingClient.create(userId, bookingCreateRequest);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void approve_whenCallApprove_thenResponseOkReturned() {
        long userId = 1L;
        long bookingId = 1L;
        boolean approved = false;

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(bookingResponse);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any(), ArgumentMatchers.<Map<String, ?>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = bookingClient.approve(userId, bookingId, approved);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void getById_whenCall_thenResponseOkReturned() {
        long userId = 1L;
        long bookingId = 1L;

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(bookingResponse);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any(), ArgumentMatchers.<Map<String, ?>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = bookingClient.getById(userId, bookingId);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void getByBookerId_whenCall_thenResponseOkReturned() {
        long userId = 0L;
        StateFilter stateFilter = StateFilter.APPROVED;
        long from = 0;
        int size = 50;

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(List.of(bookingResponse));

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any(), ArgumentMatchers.<Map<String, ?>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = bookingClient.getByBookerId(userId, stateFilter, from, size);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void getByOwner_whenCall_thenResponseOkReturned() {
        long userId = 0L;
        StateFilter stateFilter = StateFilter.APPROVED;
        long from = 0;
        int size = 50;

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(List.of(bookingResponse));

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                ArgumentMatchers.<Class<Object>>any(), ArgumentMatchers.<Map<String, ?>>any()))
                .thenReturn(responseEntity);

        ResponseEntity<Object> response = bookingClient.getByOwner(userId, stateFilter, from, size);

        MatcherAssert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }
}