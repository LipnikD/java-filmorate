package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Некорректная дата релиза фильма с идентификатором: " + film.getId());
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм {}", film.getId());
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        if (!films.containsKey(newFilm.getId())) {
            throw new NotFoundException("Не найден фильм с идентификатором: " + newFilm.getId());
        }
        films.put(newFilm.getId(), newFilm);
        log.info("Обновлены данные фильма с идентификатором {}", newFilm.getId());
        return newFilm;
    }

    @Override
    public List<Film> findAll() {
        log.info("Получен список всех фильмов");
        return new ArrayList<>(films.values());
    }

    @Override
    public List<Film> getTopPopular(int count) {
        log.info("Получение {} самых популярных фильмов", count);
        return films.values()
                .stream()
                .sorted(new FilmService.FilmLikeRankingComparator().reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film get(Long id) {
        log.info("Получение фильма по идентификатору {}", id);

        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException("Не найден фильм с идентификатором: " + id);
        }
    }

    private Long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
