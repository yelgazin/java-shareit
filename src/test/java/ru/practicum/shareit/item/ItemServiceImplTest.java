package ru.practicum.shareit.item;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.common.exception.EntityNotFoundException;
import ru.practicum.shareit.common.exception.ForbiddenException;
import ru.practicum.shareit.helper.ObjectGenerator;
import ru.practicum.shareit.item.exception.ItemException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    private final ObjectGenerator objectGenerator = new ObjectGenerator();

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Spy
    private ItemCopierImpl itemCopier;

    private Item item;
    private User user;
    private Comment comment;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = objectGenerator.next(User.class);
        item = objectGenerator.next(Item.class);
        comment = objectGenerator.next(Comment.class);
        booking = objectGenerator.next(Booking.class);

        user.setId(1L);
        item.setId(1L);
        comment.setId(1L);
        booking.setId((1L));
    }

    @Test
    void getByUserId_whenValidParameters_thenListReturned() {
        long userId = 1L;
        long from = 0;
        int size = 10;

        when(itemRepository.getByOwnerIdOrderByIdAsc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(item)));

        itemService.getByUserId(userId, from, size);

        verify(itemRepository).getByOwnerIdOrderByIdAsc(anyLong(), any());
    }

    @Test
    void getById_whenValidParameters_thenItemReturned() {
        long itemId = 1L;

        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));

        itemService.getById(itemId);

        verify(itemRepository).findById(itemId);
    }

    @Test
    void getById_whenInvalidItemId_thenEntityNotFoundExceptionThrown() {
        long itemId = 0L;

        when(itemRepository.findById(itemId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.getById(itemId));

        verify(itemRepository).findById(itemId);
    }

    @Test
    void create_whenUserExists_thenCreatedItemReturned() {
        long userId = 1L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRepository.save(item))
                .thenReturn(item);

        Item createdItem = itemService.create(userId, item);

        MatcherAssert.assertThat(createdItem.getOwner(), equalTo(user));
        verify(userRepository).findById(userId);
        verify(itemRepository).save(item);
    }

    @Test
    void create_whenUserNotExists_thenEntityNotFoundExceptionThrown() {
        long userId = 1L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.create(userId, item));

        verify(userRepository).findById(userId);
        verify(itemRepository, never()).save(item);
    }

    @Test
    void update_whenItemExistsAndUserIsOwner_thenUpdatedItemReturned() {
        long userId = 1L;
        long itemId = 1L;
        item.setOwner(user);

        Item updateItem = new Item();
        updateItem.setName("Updated name");
        updateItem.setDescription("Updated description");
        updateItem.setAvailable(false);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        when(itemRepository.save(item))
                .thenReturn(item);

        item = itemService.create(userId, item);
        item = itemService.update(userId, itemId, updateItem);

        MatcherAssert.assertThat(item.getName(), equalTo(updateItem.getName()));
        MatcherAssert.assertThat(item.getDescription(), equalTo(updateItem.getDescription()));
        MatcherAssert.assertThat(item.getAvailable(), equalTo(updateItem.getAvailable()));
        verify(userRepository).findById(userId);
        verify(itemRepository).findById(itemId);
        verify(itemRepository, times(2)).save(item);
    }

    @Test
    void update_whenItemExistsAndUserIsNotOwner_thenForbiddenExceptionThrown() {
        long userId = 2L;
        long itemId = 1L;
        item.setOwner(user);

        Item updateItem = new Item();
        updateItem.setName("Updated name");
        updateItem.setDescription("Updated description");
        updateItem.setAvailable(false);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        when(itemRepository.save(item))
                .thenReturn(item);

        item = itemService.create(userId, item);

        assertThrows(ForbiddenException.class, () -> itemService.update(userId, itemId, updateItem));
        verify(userRepository).findById(userId);
        verify(itemRepository).findById(itemId);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void update_whenItemNotExists_thenEntityNotFoundExceptionThrown() {
        long userId = 2L;
        long itemId = 1L;
        item.setOwner(user);

        Item updateItem = new Item();
        updateItem.setName("Updated name");
        updateItem.setDescription("Updated description");
        updateItem.setAvailable(false);

        when(itemRepository.findById(itemId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.update(userId, itemId, updateItem));
        verify(itemRepository).findById(itemId);
        verify(itemRepository, never()).save(item);
    }

    @Test
    void findAvailableBySubstring_whenValidParameters_thenListReturned() {
        String text = "search";
        long from = 0;
        int size = 10;

        when(itemRepository.findAvailableBySubstring(anyString(), any()))
                .thenReturn(new PageImpl<>(List.of(item)));

        itemService.findAvailableBySubstring(text, from, size);

        verify(itemRepository).findAvailableBySubstring(anyString(), any());
    }

    @Test
    void addComment_whenValidParameters_thenCreatedCommentReturned() {
        long userId = user.getId();
        long itemId = item.getId();
        booking.setItem(item);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        when(bookingRepository.findByBookerIdAndStartLessThanAndStatusIsOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));

        itemService.addComment(userId, itemId, comment);

        verify(commentRepository).save(comment);
    }

    @Test
    void addComment_whenInvalidUserId_thenItemExceptionThrown() {
        long userId = user.getId();
        long itemId = item.getId();
        booking.setItem(item);

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(ItemException.class, () -> itemService.addComment(userId, itemId, comment));
        verify(commentRepository, never()).save(comment);
    }

    @Test
    void addComment_whenItemId_thenItemExceptionThrown() {
        long userId = user.getId();
        long itemId = item.getId();
        booking.setItem(item);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.empty());

        assertThrows(ItemException.class, () -> itemService.addComment(userId, itemId, comment));
        verify(commentRepository, never()).save(comment);
    }

    @Test
    void addComment_whenItemWasNotBookedInPast_thenItemExceptionThrown() {
        long userId = user.getId();
        long itemId = item.getId();
        booking.setItem(item);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        when(bookingRepository.findByBookerIdAndStartLessThanAndStatusIsOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of());

        assertThrows(ItemException.class, () -> itemService.addComment(userId, itemId, comment));
        verify(commentRepository, never()).save(comment);
    }

}