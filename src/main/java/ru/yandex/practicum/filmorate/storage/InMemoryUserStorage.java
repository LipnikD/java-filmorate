package ru.yandex.practicum.filmorate.storage;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
            log.info("При создании пользователя с идентификатором {} не указано имя (поле 'name'). Вместо имени использован логин (поле 'login')",
                    user.getId());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь с идентификатором {}", user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Не найден пользователь с идентификатором: " + user.getId());
        }
        users.put(user.getId(), user);
        log.info("Обновлены данные пользователя с идентификатором {}", user.getId());
        return user;
    }

    @Override
    public List<User> findAll() {
        log.info("Получен список всех пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User get(Long id) {
        log.info("Получение пользователя по идентификатору {}", id);

        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Не найден пользователь с идентификатором: " + id);
        }
    }

    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
