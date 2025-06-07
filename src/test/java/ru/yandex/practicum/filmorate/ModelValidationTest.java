package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

class ModelValidationTest {

    // Валидация User
    @Test
    void testUserValidData() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        assertDoesNotThrow(() -> validateUser(user));
    }

    @Test
    void testUserInvalidEmail() {
        User user = new User();
        user.setEmail("invalidemail");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> validateUser(user));
    }

    @Test
    void testUserEmptyLogin() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin(" ");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> validateUser(user));
    }

    @Test
    void testUserBirthdayInFuture() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> validateUser(user));
    }

    @Test
    void testUserBirthdayToday() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.now());
        assertDoesNotThrow(() -> validateUser(user));
    }

    // Валидация Film
    @Test
    void testFilmValidData() {
        Film film = new Film();
        film.setName("Valid Film");
        film.setDescription("A description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        assertDoesNotThrow(() -> validateFilm(film));
    }

    @Test
    void testFilmEmptyName() {
        Film film = new Film();
        film.setName("");
        film.setDescription("A description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        assertThrows(ValidationException.class, () -> validateFilm(film));
    }

    @Test
    void testFilmDescriptionTooLong() {
        Film film = new Film();
        film.setName("Film");
        film.setDescription("a".repeat(201));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        assertThrows(ValidationException.class, () -> validateFilm(film));
    }

    @Test
    void testFilmReleaseDateTooEarly() {
        Film film = new Film();
        film.setName("Film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1800, 1, 1));
        film.setDuration(120);
        assertThrows(ValidationException.class, () -> validateFilm(film));
    }

    @Test
    void testFilmNegativeDuration() {
        Film film = new Film();
        film.setName("Film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-10);
        assertThrows(ValidationException.class, () -> validateFilm(film));
    }

    @Test
    void testFilmZeroDuration() {
        Film film = new Film();
        film.setName("Film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(0);
        assertThrows(ValidationException.class, () -> validateFilm(film));
    }

    // Методы валидации
    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Некорректная электронная почта");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Некорректный логин");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения в будущем");
        }
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Описание не может превышать 200 символов");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }
        if (film.getDuration() == null || film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность должна быть положительным числом");
        }
    }
}
