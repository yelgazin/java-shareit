package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> getByOwnerIdOrderByIdAsc(long id);

    @Query("select i from Item i where i.available = true and length(:searchText) > 0 and" +
            "(lower(i.name) like lower(concat('%', :searchText, '%')) or " +
            "lower(i.description) like lower(concat('%', :searchText, '%')))")
    List<Item> findAvailableBySubstring(String searchText);
}
