package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int currentId = 1;

    @Override
    public synchronized User add(User user) {
        user.setId(currentId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public synchronized User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NoSuchElementException("Пользователь не найден");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public synchronized void delete(Integer id) {
        users.remove(id);
    }

    @Override
    public synchronized User getById(Integer id) {
        if (!users.containsKey(id)) {
            throw new NoSuchElementException("Пользователь не найден");
        }
        return users.get(id);
    }

    @Override
    public synchronized Collection<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
