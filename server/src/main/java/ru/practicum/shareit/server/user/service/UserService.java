package ru.practicum.shareit.server.user.service;

import ru.practicum.shareit.server.user.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(long id);

    User create(User user);

    User update(long id, User user);

    void delete(long id);
}
