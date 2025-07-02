package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmController(FilmService filmService, MpaStorage mpaStorage,
                          GenreStorage genreStorage) {
        this.filmService = filmService;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Запрос на создание фильма: {}", film);
        Film createdFilm = filmService.create(film);
        log.info("Создан новый фильм: {}", createdFilm);
        return createdFilm;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Запрос на обновление фильма: {}", film);
        Film updatedFilm = filmService.update(film);
        log.info("Обновлен фильм: {}", updatedFilm);
        return updatedFilm;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable int id) {
        return filmService.getById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Запрос на добавление лайка фильму {} от пользователя {}", id, userId);
        filmService.addLike(id, userId);
        log.info("Пользователь {} добавил лайк фильму {}", userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        filmService.removeLike(id, userId);
        log.info("Пользователь {} убрал лайк с фильма {}", userId, id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Запрос {} популярных фильмов", count);
        return filmService.getPopularFilms(count);
    }
}
