package ru.practicum.shareit.gateway.item.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.common.client.BaseClient;
import ru.practicum.shareit.server.item.dto.CommentCreateRequest;
import ru.practicum.shareit.server.item.dto.ItemCreateRequest;
import ru.practicum.shareit.server.item.dto.ItemUpdateRequest;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getByUserId(long userId, long from, int size) {
        return get("?from={from}&size={size}", userId, Map.of("from", from, "size", size));
    }

    public ResponseEntity<Object> getById(long userId, long id) {
        return get("/{id}", userId, Map.of("id", id));
    }

    public ResponseEntity<Object> create(long userId, ItemCreateRequest itemCreateRequest) {
        return post("", userId, itemCreateRequest);
    }

    public ResponseEntity<Object> update(long userId, long id, ItemUpdateRequest itemUpdateRequest) {
        return patch("/{id}", userId, Map.of("id", id), itemUpdateRequest);
    }

    public ResponseEntity<Object> search(long userId, String text, long from, int size) {
        return get("/search?text={text}&from={from}&size={size}",
                userId,
                Map.of("text", text,
                       "from", from,
                       "size", size));
    }

    public ResponseEntity<Object> addComment(long userId, long itemId, CommentCreateRequest commentCreateRequest) {
        return post("/{itemId}/comment", userId, Map.of("itemId", itemId), commentCreateRequest);
    }
}