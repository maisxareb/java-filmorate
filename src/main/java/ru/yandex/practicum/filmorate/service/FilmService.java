package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getById(filmId);
        if (film.getLikes().contains(userId)) {
            throw new IllegalArgumentException("Пользователь уже поставил лайк");
        }
        film.getLikes().add(userId);
        return film;
    }

    public Film removeLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getById(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new IllegalArgumentException("Лайк от пользователя не найден");
        }
        film.getLikes().remove(userId);
        return film;
    }

    public List<Film> getPopularFilms(int count) {
        return ((ru.yandex.practicum.filmorate.storage.FilmStorage) filmStorage).getPopular(count);
    }
}
