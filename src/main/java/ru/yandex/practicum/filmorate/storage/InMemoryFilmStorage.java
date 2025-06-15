package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;

    @Override
    public synchronized Film add(Film film) {
        film.setId(currentId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public synchronized Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NoSuchElementException("Фильм не найден");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public synchronized void delete(Integer id) {
        films.remove(id);
    }

    @Override
    public synchronized Film getById(Integer id) {
        if (!films.containsKey(id)) {
            throw new NoSuchElementException("Фильм не найден");
        }
        return films.get(id);
    }

    @Override
    public synchronized Collection<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public synchronized List<Film> getPopular(int count) {
        return films.values().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
