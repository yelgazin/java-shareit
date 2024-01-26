package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> getByOwnerIdOrderByIdAsc(long id, PageRequest pageRequest);

    @Query("select i from Item i where i.available = true and length(:searchText) > 0 and" +
            "(lower(i.name) like lower(concat('%', :searchText, '%')) or " +
            "lower(i.description) like lower(concat('%', :searchText, '%')))")
    Page<Item> findAvailableBySubstring(String searchText, PageRequest pageRequest);
}