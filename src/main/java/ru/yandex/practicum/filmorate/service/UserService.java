package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    // Map для хранения друзей каждого пользователя
    private final Map<Integer, Set<Integer>> friendsMap = new HashMap<>();

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(Integer userId, Integer friendId) {
        getOrCreateFriendsSet(userId).add(friendId);
        getOrCreateFriendsSet(friendId).add(userId);
        return userStorage.getById(userId);
    }

    public User removeFriend(Integer userId, Integer friendId) {
        getOrCreateFriendsSet(userId).remove(friendId);
        getOrCreateFriendsSet(friendId).remove(userId);
        return userStorage.getById(userId);
    }

    public List<User> getFriends(Integer userId) {
        Set<Integer> friendsIds = getOrCreateFriendsSet(userId);
        return friendsIds.stream()
                .map(id -> userStorage.getById(id))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
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
}
