package ru.practicum.shareit.gateway.booking.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.common.client.BaseClient;
import ru.practicum.shareit.server.booking.entity.StateFilter;
import ru.practicum.shareit.server.booking.dto.BookingCreateRequest;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(long userId, BookingCreateRequest bookingCreateRequest) {
        return post("", userId, bookingCreateRequest);
    }

    public ResponseEntity<Object> approve(long userId, long bookingId, boolean approved) {
        Map<String, Object> parameters = Map.of(
                "bookingId", bookingId,
                "approved", approved
        );
        return patch("/{bookingId}?approved={approved}", userId, parameters, null);
    }

    public ResponseEntity<Object> getById(long userId, long bookingId) {
        return get("/{bookingId}", userId, Map.of("bookingId", bookingId));
    }

    public ResponseEntity<Object> getByBookerId(long userId,
                                                StateFilter stateFilter,
                                                long from,
                                                int size) {
        Map<String, Object> parameters = Map.of(
                "state", stateFilter.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getByOwner(long userId,
                                             StateFilter stateFilter,
                                             long from,
                                             int size) {
        Map<String, Object> parameters = Map.of(
                "state", stateFilter.name(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }
}