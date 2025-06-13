package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        Film created = filmService.addFilm(film);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        Film updated = filmService.updateFilm(film);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilms() {
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilm(@PathVariable Integer id) {
        Film film = filmService.getFilmById(id);
        return ResponseEntity.ok(film);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getPopular(@RequestParam(defaultValue = "10") int count) {
        return ResponseEntity.ok(filmService.getPopularFilms(count));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> likeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> unlikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.removeLike(id, userId);
        return ResponseEntity.ok().build();
    }
}
