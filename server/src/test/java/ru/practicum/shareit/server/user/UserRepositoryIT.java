package ru.practicum.shareit.server.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.server.helper.ObjectGenerator;
import ru.practicum.shareit.server.user.entity.User;
import ru.practicum.shareit.server.user.repository.UserRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(ObjectGenerator.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserRepositoryIT {

    private final UserRepository userRepository;
    private User user1;
    private User user2;

    private final ObjectGenerator objectGenerator;

    @BeforeEach
    void setUp() {
        user1 = objectGenerator.next(User.class);
        user2 = objectGenerator.next(User.class);
    }

    @Test
    void getByEmail_whenUserNotFound_thenEmptyOptionalReturned() {
        String searchMail = "not_valid@mail.ru";
        userRepository.save(user1);
        userRepository.save(user2);

        Optional<User> foundUser = userRepository.getByEmail(searchMail);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void getByEmail_whenUserFound_thenUserOptionalReturned() {
        String searchMail = user2.getEmail();
        userRepository.save(user1);
        userRepository.save(user2);

        Optional<User> foundUser = userRepository.getByEmail(searchMail);

        assertTrue(foundUser.isPresent());
        assertThat(foundUser.get(), equalTo(user2));
    }

    @Test
    void save_whenEmailIsNull_thenDataIntegrityViolationExceptionThrown() {
        user1.setEmail(null);
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user1));
    }

    @Test
    void save_whenNameIsNull_thenDataIntegrityViolationExceptionThrown() {
        user1.setName(null);
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user1));
    }

    @Test
    void save_whenDuplicateEmail_thenDataIntegrityViolationExceptionThrown() {
        String email = "some@mail.ru";
        user1.setEmail(email);
        user2.setEmail(email);

        userRepository.save(user1);
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user2));
    }
}