package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final Map<Integer, Set<Integer>> friendsMap = new HashMap<>();

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        validateUser(user);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userStorage.add(user);
    }

    public User updateUser(User user) {
        validateUser(user);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userStorage.update(user);
    }

    public User getUserById(Integer id) {
        if (id == null) {
            throw new ValidationException("ID пользователя не может быть null");
        }
        return userStorage.getById(id);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAll();
    }

    public void addFriend(Integer userId, Integer friendId) {
        if (userId == null || friendId == null) {
            throw new ValidationException("ID пользователя и друга не могут быть null");
        }
        getOrCreateFriendsSet(userId).add(friendId);
        getOrCreateFriendsSet(friendId).add(userId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        if (userId == null || friendId == null) {
            throw new ValidationException("ID пользователя и друга не могут быть null");
        }
        getOrCreateFriendsSet(userId).remove(friendId);
        getOrCreateFriendsSet(friendId).remove(userId);
    }

    public Collection<User> getFriends(Integer userId) {
        if (userId == null) {
            throw new ValidationException("ID пользователя не может быть null");
        }
        Set<Integer> friendsIds = getOrCreateFriendsSet(userId);
        return friendsIds.stream()
                .map(id -> userStorage.getById(id))
                .collect(Collectors.toList());
    }

    public Collection<User> getCommonFriends(Integer userId, Integer otherId) {
        if (userId == null || otherId == null) {
            throw new ValidationException("ID пользователя не может быть null");
        }
        Set<Integer> friends1 = getOrCreateFriendsSet(userId);
        Set<Integer> friends2 = getOrCreateFriendsSet(otherId);
        return friends1.stream()
                .filter(friends2::contains)
                .map(id -> userStorage.getById(id))
                .collect(Collectors.toList());
    }

    private Set<Integer> getOrCreateFriendsSet(Integer userId) {
        return friendsMap.computeIfAbsent(userId, k -> new HashSet<>());
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный email");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Некорректный login");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(java.time.LocalDate.now())) {
            throw new ValidationException("Дата рождения в будущем");
        }
    }
}
