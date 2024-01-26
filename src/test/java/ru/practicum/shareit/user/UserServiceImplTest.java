package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.common.exception.EntityNotFoundException;
import ru.practicum.shareit.helper.ObjectGenerator;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private UserCopierImpl userCopier;

    @InjectMocks
    private UserServiceImpl userService;

    private final ObjectGenerator objectGenerator = new ObjectGenerator();

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = objectGenerator.next(User.class);
        user2 = objectGenerator.next(User.class);

        user1.setId(1L);
        user2.setId(2L);
    }

    @Test
    void getAll_whenUsersExist_thenNonEmptyListReturned() {
        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        List<User> actualUsers = userService.getAll();

        assertThat(actualUsers, hasSize(2));
        verify(userRepository).findAll();
    }

    @Test
    void getAll_whenUsersNotExist_thenEmptyListReturned() {
        when(userRepository.findAll())
                .thenReturn(List.of());

        List<User> actualUsers = userService.getAll();

        assertThat(actualUsers, hasSize(0));
        verify(userRepository).findAll();
    }

    @Test
    void getById_whenUserExists_thenUserReturned() {
        long userId = 1L;
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user1));

        User actualUser = userService.getById(userId);

        assertThat(actualUser, is(user1));
        verify(userRepository).findById(userId);
    }

    @Test
    void getById_whenUserNotExists_thenEntityNotFoundExceptionThrown() {
        long userId = 1L;
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getById(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    void create_whenProvidedNewUser_thenCreatedUserReturned() {
        userService.create(user1);

        verify(userRepository).save(user1);
    }

    @Test
    void update_whenUserNotExists_thenEntityNotFoundExceptionReturned() {
        long userId = 1L;
        User updateUser = new User();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.update(userId, updateUser));
        verify(userRepository).findById(userId);
        verify(userCopier, never()).update(any(), any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void update_whenUserExistsAndUpdateOnlyName_thenUpdatedUserReturned() {
        long userId = user1.getId();
        User updateUser = new User();
        updateUser.setName("Updated name");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user1));
        when(userRepository.save(user1))
                .thenReturn(user1);

        userService.update(userId, updateUser);

        assertThat(user1.getName(), equalTo(updateUser.getName()));
        assertThat(user1.getEmail(), not(equalTo(updateUser.getEmail())));

        InOrder inOrder = inOrder(userRepository, userCopier);
        inOrder.verify(userRepository).findById(userId);
        inOrder.verify(userCopier).update(user1, updateUser);
        inOrder.verify(userRepository).save(user1);
    }

    @Test
    void update_whenUserExistsAndUpdateOnlyEmail_thenUpdatedUserReturned() {
        long userId = user1.getId();
        User updateUser = new User();
        updateUser.setEmail("updated_name@mail.com");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user1));
        when(userRepository.save(user1))
                .thenReturn(user1);

        userService.update(userId, updateUser);

        assertThat(user1.getName(), not(equalTo(updateUser.getName())));
        assertThat(user1.getEmail(), equalTo(updateUser.getEmail()));

        InOrder inOrder = inOrder(userRepository, userCopier);
        inOrder.verify(userRepository).findById(userId);
        inOrder.verify(userCopier).update(user1, updateUser);
        inOrder.verify(userRepository).save(user1);
    }

    @Test
    void delete_whenUserExistsOrNot_thenNothingReturned() {
        long userId = 1L;

        userService.delete(userId);

        verify(userRepository).deleteById(userId);
    }
}