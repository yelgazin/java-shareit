package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.common.exception.EntityNotFoundException;
import ru.practicum.shareit.helper.ObjectGenerator;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    private final ObjectGenerator objectGenerator = new ObjectGenerator();

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private User user;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        user = objectGenerator.next(User.class);
        itemRequest = objectGenerator.next(ItemRequest.class);
    }

    @Test
    void create_whenUserExists_thenCreatedItemRequestReturned() {
        long userId = 1L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        itemRequestService.create(userId, itemRequest);

        verify(userRepository).findById(userId);
        verify(itemRequestRepository).save(itemRequest);
    }

    @Test
    void create_whenUserNotExists_thenEntityNotFoundExceptionThrown() {
        long userId = 1L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemRequestService.create(userId, itemRequest));

        verify(userRepository).findById(userId);
        verify(itemRequestRepository, never()).save(itemRequest);
    }


    @Test
    void getAllMyRequests_whenUserExists_thenListOfItemRequestReturned() {
        long userId = 1L;

        when(userRepository.existsById(userId))
                .thenReturn(true);
        when(itemRequestRepository.findAllByAuthorIdOrderByCreatedDesc(userId))
                .thenReturn(List.of(itemRequest));

        itemRequestService.getAllMyRequests(userId);

        verify(userRepository).existsById(userId);
        verify(itemRequestRepository).findAllByAuthorIdOrderByCreatedDesc(userId);
    }

    @Test
    void getAllMyRequests_whenUserNotExists_thenEntityNotFoundExceptionThrown() {
        long userId = 1L;

        when(userRepository.existsById(userId))
                .thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> itemRequestService.getAllMyRequests(userId));

        verify(userRepository).existsById(userId);
        verify(itemRequestRepository, never()).findAllByAuthorIdOrderByCreatedDesc(userId);
    }

    @Test
    void getAllNotUserRequests_whenUserExists_thenListOfItemRequestReturned() {
        long userId = 1L;
        long from = 0;
        int size = 50;

        when(userRepository.existsById(userId))
                .thenReturn(true);
        when(itemRequestRepository.findAllByAuthorIdNot(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(itemRequest)));

        itemRequestService.getAllNotUserRequests(userId, from, size);

        verify(userRepository).existsById(userId);
        verify(itemRequestRepository).findAllByAuthorIdNot(anyLong(), any());
    }

    @Test
    void getAllNotUserRequests_whenUserNotExists_thenEntityNotFoundExceptionThrown() {
        long userId = 1L;
        long from = 0;
        int size = 50;

        when(userRepository.existsById(userId))
                .thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> itemRequestService.getAllNotUserRequests(userId, from, size));

        verify(userRepository).existsById(userId);
        verify(itemRequestRepository, never()).findAllByAuthorIdNot(anyLong(), any());
    }

    @Test
    void getByRequestId_whenUserAndItemRequestExist_thenItemRequestReturned() {
        long userId = 1L;
        long requestId = 1L;

        when(userRepository.existsById(userId))
                .thenReturn(true);
        when(itemRequestRepository.findById(requestId))
                .thenReturn(Optional.of(itemRequest));

        itemRequestService.getByRequestId(userId, requestId);

        verify(userRepository).existsById(userId);
        verify(itemRequestRepository).findById(requestId);
    }

    @Test
    void getByRequestId_whenUserNotExists_thenEntityNotFoundExceptionThrown() {
        long userId = 0L;
        long requestId = 1L;

        when(userRepository.existsById(userId))
                .thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> itemRequestService.getByRequestId(userId, requestId));

        verify(userRepository).existsById(userId);
        verify(itemRequestRepository, never()).findById(requestId);
    }

    @Test
    void getByRequestId_whenItemRequestNotExists_thenEntityNotFoundExceptionThrown() {
        long userId = 1L;
        long requestId = 1L;

        when(userRepository.existsById(userId))
                .thenReturn(true);
        when(itemRequestRepository.findById(requestId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemRequestService.getByRequestId(userId, requestId));

        verify(userRepository).existsById(userId);
        verify(itemRequestRepository).findById(requestId);
    }

}