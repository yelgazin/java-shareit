package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void getByEmail() {
        String email = "ivan@mail.ru";

        User user = new User();
        user.setName("Иван");
        user.setEmail(email);

        userRepository.save(user);
        Optional<User> actual = userRepository.getByEmail(email);
        assertTrue(actual.isPresent());
        assertEquals("Иван", actual.get().getName());
        assertEquals(email, actual.get().getEmail());
    }
}