package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.jdbc.LikeDao;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeDao likeDao;

    @Autowired
    public FilmService(
            @Qualifier("filmDbStorage") FilmStorage filmStorage,
            @Qualifier("userDbStorage") UserStorage userStorage,
            LikeDao likeDao) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeDao = likeDao;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(int id) {
        return filmStorage.getById(id);
    }


    public void addLike(int filmId, int userId) {
        Film film = getFilmOrThrow(filmId);
        getUserOrThrow(userId);
        likeDao.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = getFilmOrThrow(filmId);
        getUserOrThrow(userId);
        likeDao.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        List<Integer> popularFilmIds = likeDao.getPopularFilms(count);
        return popularFilmIds.stream()
                .map(filmStorage::getById)
                .collect(Collectors.toList());
    }

    private Film getFilmOrThrow(int id) {
        return filmStorage.getById(id);
    }

    private void getUserOrThrow(int id) {
        if (userStorage.getById(id) == null) {
            throw new UserNotFoundException("Пользователь с id=" + id + " не найден");
        }
    }
}