package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends ResourceNotFoundException {
    public FilmNotFoundException(String message) {
        super(message);
    }
}