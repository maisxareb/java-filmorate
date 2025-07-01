package ru.yandex.practicum.filmorate.exception;

public class GenreNotFoundException extends ResourceNotFoundException {
    public GenreNotFoundException(String message) {
        super(message);
    }
}