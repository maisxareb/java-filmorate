package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Film add(Film film);

    Film update(Film film);

    void delete(Integer id);

    Film getById(Integer id);

    Collection<Film> getAll();

    List<Film> getPopular(int count);
}
