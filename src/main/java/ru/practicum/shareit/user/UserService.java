package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(long id);

    User create(User user);

    User update(long id, User user);

    void delete(long id);
}
