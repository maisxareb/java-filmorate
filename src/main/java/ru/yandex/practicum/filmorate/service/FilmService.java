package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final UserService userService;
    private final FilmStorage filmStorage;

    public Film addFilm(Film film) {
        validateFilm(film);
        return filmStorage.add(film);
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        return filmStorage.update(film);
    }

    public Film getFilmById(Integer id) {
        if (id == null) {
            throw new ValidationException("ID фильма не может быть null");
        }
        return filmStorage.getById(id);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopular(count);
    }

    public void addLike(Integer filmId, Integer userId) {
        userService.getUserById(userId);
        Film film = getFilmById(filmId);
        if (film.getLikes().contains(userId)) {
            throw new IllegalArgumentException("Пользователь уже поставил лайк");
        }
        film.getLikes().add(userId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        userService.getUserById(userId);
        Film film = getFilmById(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new LikeNotFoundException("Лайк от пользователя не найден");
        }
        film.getLikes().remove(userId);
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
