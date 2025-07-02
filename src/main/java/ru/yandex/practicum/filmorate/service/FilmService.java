package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.jdbc.LikeDao;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeDao likeDao;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(
            @Qualifier("filmDbStorage") FilmStorage filmStorage,
            @Qualifier("userDbStorage") UserStorage userStorage,
            LikeDao likeDao,
            MpaStorage mpaStorage,
            GenreStorage genreStorage
    ) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeDao = likeDao;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    public Film create(Film film) {
        validateFilm(film);
        validateMpa(film.getMpa().getId());
        validateGenres(film.getGenres());
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validateFilm(film);
        validateMpa(film.getMpa().getId());
        validateGenres(film.getGenres());
        return filmStorage.update(film);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(int id) {
        return filmStorage.getById(id);
    }

    public void addLike(int filmId, int userId) {
        getFilmOrThrow(filmId);
        getUserOrThrow(userId);
        likeDao.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        getFilmOrThrow(filmId);
        getUserOrThrow(userId);
        likeDao.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        List<Integer> popularFilmIds = likeDao.getPopularFilms(count);
        return popularFilmIds.stream()
                .map(filmStorage::getById)
                .collect(Collectors.toList());
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Название фильма не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Описание превышает 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate() == null) {
            log.error("Дата релиза не указана");
            throw new ValidationException("Дата релиза обязательна");
        }
        if (film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            log.error("Некорректная дата релиза: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.error("Некорректная продолжительность: {}", film.getDuration());
            throw new ValidationException("Продолжительность должна быть положительной");
        }
    }

    private void validateMpa(int mpaId) {
        try {
            mpaStorage.getById(mpaId);
        } catch (MpaNotFoundException e) {
            throw new MpaNotFoundException("Рейтинг MPA с id=" + mpaId + " не найден");
        }
    }

    private void validateGenres(java.util.Set<ru.yandex.practicum.filmorate.model.Genre> genres) {
        if (genres != null) {
            for (ru.yandex.practicum.filmorate.model.Genre genre : genres) {
                try {
                    genreStorage.getById(genre.getId());
                } catch (GenreNotFoundException e) {
                    throw new GenreNotFoundException("Жанр с id=" + genre.getId() + " не найден");
                }
            }
        }
    }

    private void getUserOrThrow(int id) {
        if (userStorage.getById(id) == null) {
            throw new UserNotFoundException("Пользователь с id=" + id + " не найден");
        }
    }

    private void getFilmOrThrow(int id) {
        if (filmStorage.getById(id) == null) {
            throw new RuntimeException("Фильм с id=" + id + " не найден");
        }
    }
}
