package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User owner1 = new User();
        owner1.setName("Name1");
        owner1.setEmail("name1@mail.ru");
        userRepository.save(owner1);

        User owner2 = new User();
        owner2.setName("Name2");
        owner2.setEmail("name2@mail.ru");
        userRepository.save(owner2);

        Item item1 = new Item();
        item1.setName("Item1");
        item1.setDescription("Description1");
        item1.setOwner(owner1);
        item1.setAvailable(false);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Item2");
        item2.setDescription("Description2");
        item2.setOwner(owner2);
        item2.setAvailable(true);
        itemRepository.save(item2);
    }

    @Test
    void getByOwnerIdOrderByIdAsc() {
        List<Item> items1 = itemRepository.getByOwnerIdOrderByIdAsc(1);
        assertEquals(1, items1.size());
        assertEquals(1, items1.get(0).getId());

        List<Item> items2 = itemRepository.getByOwnerIdOrderByIdAsc(2);
        assertEquals(1, items2.size());
        assertEquals(2, items2.get(0).getId());
    }

    @Test
    void findAvailableBySubstring() {
        List<Item> items = itemRepository.findAvailableBySubstring("script");
        assertEquals(1, items.size());
        assertEquals(2, items.get(0).getId());
    }
}