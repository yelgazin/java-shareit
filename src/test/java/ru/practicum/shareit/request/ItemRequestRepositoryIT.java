package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.helper.ObjectGenerator;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import org.hamcrest.Matchers;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

@DataJpaTest
@Import(ObjectGenerator.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestRepositoryIT {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ObjectGenerator objectGenerator;

    private User author1;
    private User author2;

    private ItemRequest itemRequest11;
    private ItemRequest itemRequest12;
    private ItemRequest itemRequest21;
    private ItemRequest itemRequest22;

    @BeforeEach
    void setUp() {
        author1 = objectGenerator.next(User.class);
        author2 = objectGenerator.next(User.class);
        userRepository.save(author1);
        userRepository.save(author2);

        itemRequest11 = objectGenerator.next(ItemRequest.class);
        itemRequest12 = objectGenerator.next(ItemRequest.class);

        itemRequest21 = objectGenerator.next(ItemRequest.class);
        itemRequest22 = objectGenerator.next(ItemRequest.class);
    }

    @Test
    void save_whenDescriptionIsNull_thenDataIntegrityViolationExceptionThrown() {
        userRepository.save(author1);
        itemRequest11.setAuthor(author1);
        itemRequest11.setDescription(null);

        assertThrows(DataIntegrityViolationException.class, () -> itemRequestRepository.save(itemRequest11));
    }

    @Test
    void save_whenAuthorIsNull_thenDataIntegrityViolationExceptionThrown() {
        itemRequest11.setAuthor(null);

        assertThrows(DataIntegrityViolationException.class, () -> itemRequestRepository.save(itemRequest11));
    }

    @Test
    void findAllByAuthorIdOrderByCreatedDesc_whenAuthor1AndTwoRequestExists_thanTwoOrderedByCreatedDescRequestsReturned() {
        itemRequest11.setAuthor(author1);
        itemRequest12.setAuthor(author1);
        itemRequest21.setAuthor(author2);
        itemRequest22.setAuthor(author2);
        itemRequestRepository.save(itemRequest11);
        itemRequestRepository.save(itemRequest12);
        itemRequestRepository.save(itemRequest21);
        itemRequestRepository.save(itemRequest22);

        List<ItemRequest> actualItems = itemRequestRepository.findAllByAuthorIdOrderByCreatedDesc(author1.getId());

        assertThat(actualItems, Matchers.hasSize(2));
        assertThat(actualItems, Matchers.contains(itemRequest12, itemRequest11));
    }

    @Test
    void findAllByAuthorIdOrderByCreatedDesc_whenAuthor2RequestNotExists_thanEmptyListReturned() {
        itemRequest11.setAuthor(author1);
        itemRequest12.setAuthor(author1);
        itemRequestRepository.save(itemRequest11);
        itemRequestRepository.save(itemRequest12);

        List<ItemRequest> actualItems = itemRequestRepository.findAllByAuthorIdOrderByCreatedDesc(author2.getId());

        assertThat(actualItems, Matchers.hasSize(0));
    }

    @Test
    void findAllByAuthorIdNot_whenAuthor1HasRequest_thenOnlyRequestsOfAuthor2Returned() {
        itemRequest11.setAuthor(author1);
        itemRequest12.setAuthor(author1);
        itemRequest21.setAuthor(author2);
        itemRequest22.setAuthor(author2);
        itemRequestRepository.save(itemRequest11);
        itemRequestRepository.save(itemRequest12);
        itemRequestRepository.save(itemRequest21);
        itemRequestRepository.save(itemRequest22);
        PageRequest pageRequest = PageRequest.of(0, 50);

        List<ItemRequest> actualItems = itemRequestRepository.findAllByAuthorIdNot(author1.getId(), pageRequest)
                .getContent();

        assertThat(actualItems, Matchers.hasSize(2));
        assertThat(actualItems, Matchers.containsInAnyOrder(itemRequest22, itemRequest21));
    }
}