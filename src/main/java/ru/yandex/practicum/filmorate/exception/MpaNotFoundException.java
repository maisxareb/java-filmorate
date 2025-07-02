package ru.yandex.practicum.filmorate.exception;

public class MpaNotFoundException extends ResourceNotFoundException {
    public MpaNotFoundException(String message) {
        super(message);
    }
}