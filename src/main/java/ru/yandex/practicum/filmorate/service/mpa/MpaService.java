package ru.yandex.practicum.filmorate.service.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaService {

    Optional<Mpa> getById(int id);

    List<Mpa> getAll();
}