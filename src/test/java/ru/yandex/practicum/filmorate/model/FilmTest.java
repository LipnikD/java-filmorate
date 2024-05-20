package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.List;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    FilmController filmController;
    Validator validator;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
        validator = buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void addGoodFilm() {
        Film film = Film.builder()
                .name("Имя фильма")
                .description("Описание фильма")
                .duration(120)
                .releaseDate(LocalDate.now().minusYears(3))
                .build();
        filmController.create(film);
        List<Film> films = filmController.findAll();
        assertEquals(1, films.size(), "Ошибка при сохранении фильма.");
        assertEquals(film.getId(), films.getFirst().getId(), "Некорректный идентификатор сохраненного фильма.");
    }

    @Test
    void updateFilm() {
        Film film = Film.builder()
                .name("Имя фильма")
                .description("Описание фильма")
                .duration(120)
                .releaseDate(LocalDate.now().minusYears(3))
                .build();
        filmController.create(film);
        film.setDescription("English version of description");
        filmController.update(film);
        List<Film> films = filmController.findAll();
        assertEquals(1, films.size(), "Ошибка при обновлении фильма.");
    }

    @Test
    void addUserWrongDescription() {
        Film film = Film.builder()
                .name("Имя фильма")
                .description("Съешь ещё этих мягких французских булок, да выпей чаю. " +
                        "Съешь ещё этих мягких французских булок, да выпей чаю. " +
                        "Съешь ещё этих мягких французских булок, да выпей чаю. " +
                        "Съешь ещё этих мягких французских булок, да выпей чаю.")
                .duration(120)
                .releaseDate(LocalDate.now().minusYears(3))
                .build();
        assertEquals(1, validator.validate(film).size(),
                "Ошибка валидации фильма с очень длинным описанием");
    }

    @Test
    void addUserWrongReleaseDate() {
        Film film = Film.builder()
                .name("Имя фильма")
                .description("Умеренное описание фильма.")
                .duration(120)
                .releaseDate(LocalDate.of(1700, 1, 1))
                .build();
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }
}