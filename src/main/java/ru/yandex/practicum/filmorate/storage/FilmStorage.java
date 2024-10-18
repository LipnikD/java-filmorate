package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film newFilm);

    List<Film> findAll();

    List<Film> getTopPopular(int count);

    Film get(Long id);

}
