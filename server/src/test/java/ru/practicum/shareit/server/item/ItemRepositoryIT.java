package ru.practicum.shareit.server.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.server.booking.repository.BookingRepository;
import ru.practicum.shareit.server.helper.ObjectGenerator;
import ru.practicum.shareit.server.item.entity.Item;
import ru.practicum.shareit.server.item.repository.ItemRepository;
import ru.practicum.shareit.server.user.entity.User;
import ru.practicum.shareit.server.user.repository.UserRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@Import(ObjectGenerator.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRepositoryIT {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final TestEntityManager testEntityManager;
    private final ObjectGenerator objectGenerator;

    private User owner1;
    private User owner2;

    private Item item11;
    private Item item12;
    private Item item21;
    private Item item22;

    @BeforeEach
    void setUp() {
        owner1 = objectGenerator.next(User.class);
        owner2 = objectGenerator.next(User.class);
        userRepository.save(owner1);
        userRepository.save(owner2);

        item11 = objectGenerator.next(Item.class);
        item12 = objectGenerator.next(Item.class);
        item21 = objectGenerator.next(Item.class);
        item22 = objectGenerator.next(Item.class);

        item11.setOwner(owner1);
        item12.setOwner(owner1);
        item21.setOwner(owner2);
        item22.setOwner(owner2);
    }

    @Test
    void getByOwnerIdOrderByIdAsc_whenItemsOfOwner2Exists_thenTwoItemOrderedByIdAscReturned() {
        itemRepository.save(item11);
        itemRepository.save(item12);
        itemRepository.save(item21);
        itemRepository.save(item22);
        PageRequest pageRequest = PageRequest.of(0, 50);

        List<Item> actualItems = itemRepository.getByOwnerIdOrderByIdAsc(owner2.getId(), pageRequest).getContent();

        assertThat(actualItems, contains(item21, item22));
    }

    @Test
    void findAvailableBySubstring_whenTwoItemMatchCriteriaAndTwoAvailable_thenTwoItemReturned() {
        String searchString = "дрель";
        item11.setDescription("Аккумуляторная дрель");
        item12.setDescription("Просто дрель");
        item21.setDescription("Горный велосипед");
        item22.setDescription("Газонокосилка");
        item11.setAvailable(true);
        item12.setAvailable(true);
        item21.setAvailable(true);
        item22.setAvailable(true);
        itemRepository.save(item11);
        itemRepository.save(item12);
        itemRepository.save(item21);
        itemRepository.save(item22);
        PageRequest pageRequest = PageRequest.of(0, 50);

        List<Item> actualItems = itemRepository.findAvailableBySubstring(searchString, pageRequest).getContent();

        assertThat(actualItems, containsInAnyOrder(item11, item12));
    }

    @Test
    void findAvailableBySubstring_whenTwoItemMatchCriteriaAndOnlyOneAvailable_thenOneItemReturned() {
        String searchString = "дрель";
        item11.setDescription("Аккумуляторная дрель");
        item12.setDescription("Просто дрель");
        item21.setDescription("Горный велосипед");
        item22.setDescription("Газонокосилка");
        item11.setAvailable(true);
        item12.setAvailable(false);
        item21.setAvailable(true);
        item22.setAvailable(true);
        itemRepository.save(item11);
        itemRepository.save(item12);
        itemRepository.save(item21);
        itemRepository.save(item22);
        PageRequest pageRequest = PageRequest.of(0, 50);

        List<Item> actualItems = itemRepository.findAvailableBySubstring(searchString, pageRequest).getContent();

        assertThat(actualItems, containsInAnyOrder(item11));
    }

    @Test
    void findAvailableBySubstring_whenItemMatchCaseInsensitiveCriteriaAndAvailable_thenOneItemReturned() {
        String searchString = "дРеЛь";
        item11.setDescription("Аккумуляторная дрель");
        item11.setAvailable(true);
        itemRepository.save(item11);
        PageRequest pageRequest = PageRequest.of(0, 50);

        List<Item> actualItems = itemRepository.findAvailableBySubstring(searchString, pageRequest).getContent();

        assertThat(actualItems, containsInAnyOrder(item11));
    }

    @Test
    void findAvailableBySubstring_whenSearchStringIsNull_thenEmptyListReturned() {
        String searchString = null;
        item11.setDescription("Аккумуляторная дрель");
        item11.setAvailable(true);
        itemRepository.save(item11);
        PageRequest pageRequest = PageRequest.of(0, 50);

        List<Item> actualItems = itemRepository.findAvailableBySubstring(searchString, pageRequest).getContent();

        assertThat(actualItems, hasSize(0));
    }

    @Test
    void findAvailableBySubstring_whenTwoItemsMatchCriteriaInNameOrDescriptionAndAvailable_thenTwoItemReturned() {
        String searchString = "дрель";
        item11.setDescription("Аккумуляторная дрель");
        item12.setName("Просто дрель");
        item21.setDescription("Горный велосипед");
        item22.setDescription("Газонокосилка");
        item11.setAvailable(true);
        item12.setAvailable(true);
        item21.setAvailable(true);
        item22.setAvailable(true);
        itemRepository.save(item11);
        itemRepository.save(item12);
        itemRepository.save(item21);
        itemRepository.save(item22);
        PageRequest pageRequest = PageRequest.of(0, 50);

        List<Item> actualItems = itemRepository.findAvailableBySubstring(searchString, pageRequest).getContent();

        assertThat(actualItems, containsInAnyOrder(item11, item12));
    }

    @Test
    void findAvailableBySubstring_whenSearchStringIsEmpty_thenEmptyListReturned() {
        String searchString = "";
        item11.setDescription("Аккумуляторная дрель");
        item11.setAvailable(true);
        itemRepository.save(item11);
        PageRequest pageRequest = PageRequest.of(0, 50);

        List<Item> actualItems = itemRepository.findAvailableBySubstring(searchString, pageRequest).getContent();

        assertThat(actualItems, hasSize(0));
    }
}