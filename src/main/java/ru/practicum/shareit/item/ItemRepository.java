package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> getByOwnerId(long id);

    @Query("select i from Item i where i.available = true and length(:searchText) > 0 and" +
            "(lower(i.name) like lower(concat('%', :searchText, '%')) or " +
            "lower(i.description) like lower(concat('%', :searchText, '%')))")
    List<Item> findAvailableBySubstring(String searchText);

//    List<Item> findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(String name, String description);

    //List<Item> findByNameOrDescriptionContainsIgnoreCase(String text);

    //@Query("SELECT t FROM Tutorial t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword,'%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword,'%'))")
    //List<Tutorial> findByTitleContainingOrDescriptionContainingCaseInsensitive(String keyword);
    //
    //@Query("SELECT t FROM Tutorial t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :title,'%')) AND t.published=:isPublished")
    //List<Tutorial> findByTitleContainingCaseInsensitiveAndPublished(String title, boolean isPublished);
}
