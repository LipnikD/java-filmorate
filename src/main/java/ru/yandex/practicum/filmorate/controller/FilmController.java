package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        log.info("Получен список всех фильмов");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            String message = "Некорректная дата релиза фильма с идентификатором: " + film.getId();
            log.error(message);
            throw new ValidationException(message);
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм " + film.getId());
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        if (!films.containsKey(newFilm.getId())) {
            String message = "Не найден фильм с идентификатором: " + newFilm.getId();
            log.error(message);
            throw new ValidationException(message);
        }
        films.put(newFilm.getId(), newFilm);
        log.info("Обновлены данные фильма с идентификатором " + newFilm.getId());
        return newFilm;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
