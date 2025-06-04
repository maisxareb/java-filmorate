package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final List<User> users = new ArrayList<>();
    private Integer currentId = 1;

    @PostMapping
    public User createUser(@RequestBody User newUser) {
        validateUser(newUser);
        newUser.setId(currentId++);
        if (newUser.getName() == null || newUser.getName().isEmpty()) {
            newUser.setName(newUser.getLogin());
        }
        users.add(newUser);
        log.info("Создан новый пользователь: {}", newUser);
        return newUser;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        validateUser(user);
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                if (user.getName() == null || user.getName().isEmpty()) {
                    user.setName(user.getLogin());
                }
                users.set(i, user);
                log.info("Пользователь обновлен: {}", user);
                return user;
            }
        }
        throw new RuntimeException("Пользователь не найден.");
    }

    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Некорректная электронная почта: {}", user.getEmail());
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Некорректный логин: {}", user.getLogin());
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        LocalDate now = LocalDate.now();
        if (user.getBirthday() != null && user.getBirthday().isAfter(now)) {
            log.error("Некорректная дата рождения: {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}
