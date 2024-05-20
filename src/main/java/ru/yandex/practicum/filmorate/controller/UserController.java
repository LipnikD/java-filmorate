package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public List<User> findAll() {
        log.info("Получен список всех пользователей");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("При создании пользователя с идентификатором " + user.getId()
                    + " не указано имя (поле 'name'). Вместо имени использован логин (поле 'login')");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь " + user.getId());
        return user;
    }

    @PutMapping
    public User update(@Valid @Validated @RequestBody User newUser) {
        if (!users.containsKey(newUser.getId())) {
            String message = "Не найден пользователь с идентификатором: " + newUser.getId();
            log.error(message);
            throw new ValidationException(message);
        }
        users.put(newUser.getId(), newUser);
        log.info("Обновлены данные пользователя с идентификатором " + newUser.getId());
        return newUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
