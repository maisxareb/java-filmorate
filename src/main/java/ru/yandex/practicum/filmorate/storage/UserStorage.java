package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;

public interface UserStorage {

    User add(User user);

    User update(User user);

    void delete(Integer id);

    User getById(Integer id);

    Collection<User> getAll();
}
