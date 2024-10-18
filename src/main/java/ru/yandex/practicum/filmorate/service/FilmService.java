package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {

    @Autowired
    private final FilmStorage filmStorage;

    @Autowired
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public List<Film> getTopPopular(Integer count) {
        return filmStorage.getTopPopular(count);
    }

    public Film like(Long id, Long userId) {
        Film film = filmStorage.get(id);
        User user = userStorage.get(userId);
        film.getLikes().add(user.getId());
        return film;
    }

    public Film unlike(Long id, Long userId) {
        Film film = filmStorage.get(id);
        User user = userStorage.get(userId);
        film.getLikes().remove(user.getId());
        return film;
    }

    public Film get(Long id) {
        return filmStorage.get(id);
    }

    public static class FilmLikeRankingComparator implements Comparator<Film> {
        @Override
        public int compare(Film firstFilm, Film secondFilm) {
            return Integer.compare(firstFilm.getLikes().size(), secondFilm.getLikes().size());
        }
    }

}
