package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    private FilmController filmController;
    Validator validator;

    @BeforeEach
    void beforeEach() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        FilmService  filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);
        validator = buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void addGoodFilm() {
        Film film = new Film();
        film.setName("Имя фильма");
        film.setDescription("Описание фильма");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.now().minusYears(3));
        filmController.create(film);
        List<Film> films = filmController.findAll();
        assertEquals(1, films.size(), "Ошибка при сохранении фильма.");
        assertEquals(film.getId(), films.getFirst().getId(), "Некорректный идентификатор сохраненного фильма.");
    }

    @Test
    void updateFilm() {
        Film film = new Film();
        film.setName("Имя фильма");
        film.setDescription("Описание фильма");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.now().minusYears(3));
        filmController.create(film);
        film.setDescription("English version of description");
        filmController.update(film);
        List<Film> films = filmController.findAll();
        assertEquals(1, films.size(), "Ошибка при обновлении фильма.");
    }

    @Test
    void addUserWrongDescription() {
        Film film = new Film();
        film.setName("Имя фильма");
        film.setDescription("Съешь ещё этих мягких французских булок, да выпей чаю. " +
                        "Съешь ещё этих мягких французских булок, да выпей чаю. " +
                        "Съешь ещё этих мягких французских булок, да выпей чаю. " +
                        "Съешь ещё этих мягких французских булок, да выпей чаю.");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.now().minusYears(3));
        assertEquals(1, validator.validate(film).size(),
                "Ошибка валидации фильма с очень длинным описанием");
    }

    @Test
    void addUserWrongReleaseDate() {
        Film film = new Film();
        film.setName("Имя фильма");
        film.setDescription("Умеренное описание фильма.");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(1700, 1, 1));
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }
}