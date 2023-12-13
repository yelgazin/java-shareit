package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.common.AbstractRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl extends AbstractRepository<Item> implements ItemRepository {

    @Override
    public List<Item> getByUserId(long id) {
        return storage.values()
                .stream()
                .filter(item -> Objects.equals(item.getOwnerId(), id))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findAvailableBySubstring(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }

        String lowercaseText = text.toLowerCase();
        Predicate<Item> nameContains = item -> Objects.nonNull(item.getName()) &&
                item.getName().toLowerCase().contains(lowercaseText);

        Predicate<Item> descriptionContains = item -> Objects.nonNull(item.getDescription()) &&
                item.getDescription().toLowerCase().contains(lowercaseText);

        return storage.values()
                .stream()
                .filter(Item::getAvailable)
                .filter(nameContains.or(descriptionContains))
                .collect(Collectors.toList());
    }
}
