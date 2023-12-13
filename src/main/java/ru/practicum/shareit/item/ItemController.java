package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemConverter;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemConverter itemConverter;
    private final ItemService itemService;

    @GetMapping
    public List<ItemResponse> getByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemConverter.convert(itemService.getByUserId(userId));
    }

    @GetMapping("/{id}")
    public ItemResponse getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        return itemConverter.convert(itemService.getById(id));
    }

    @PostMapping
    public ItemResponse create(@RequestHeader("X-Sharer-User-Id") long userId,
                               @Valid @RequestBody ItemCreateRequest itemCreateRequest) {
        return itemConverter.convert(
                itemService.create(userId, itemConverter.convertCreateRequestDto(itemCreateRequest)));
    }

    @PatchMapping("/{id}")
    public ItemResponse update(@RequestHeader("X-Sharer-User-Id") long userId,
                    @PathVariable long id,
                    @Valid @RequestBody ItemUpdateRequest itemUpdateRequest) {
        return itemConverter.convert(
                itemService.update(userId, id, itemConverter.convertUpdateRequestDto(itemUpdateRequest)));
    }

    @GetMapping("/search")
    public List<ItemResponse> search(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @RequestParam String text) {
        return itemConverter.convert(itemService.findAvailableBySubstring(text));
    }
}