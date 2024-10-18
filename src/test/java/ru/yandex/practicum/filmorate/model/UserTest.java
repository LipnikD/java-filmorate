package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    UserController userController;
    UserService userService;
    Validator validator;

    @BeforeEach
    void beforeEach() {
        UserStorage userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        userController = new UserController(userService);
        validator = buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void addGoodUser() {
        User user = new User();
        user.setEmail("jhbdfhgbehbkgjhb@ya.ru");
        user.setLogin("login_jhdsjkfbhbh");
        user.setName("name_njkfsdnif");
        user.setBirthday(LocalDate.now().minusYears(20));
        userController.create(user);
        List<User> users = userController.findAll();
        assertEquals(1, users.size(), "Ошибка при сохранении пользователя.");
        assertEquals(user.getId(), users.getFirst().getId(), "Некорректный идентификатор сохраненного пользователя.");
    }

    @Test
    void updateUser() {
        User user = new User();
        user.setEmail("jhbdfhgbehbkgjhb@ya.ru");
        user.setLogin("login_jhdsjkfbhbh");
        user.setName("name_njkfsdnif");
        user.setBirthday(LocalDate.now().minusYears(20));
        userController.create(user);
        user.setName("name_updated");
        userController.update(user);
        List<User> users = userController.findAll();
        assertEquals(1, users.size(), "Ошибка при обновлении пользователя.");
    }

    @Test
    void addEmptyUser() {
        User user = new User();
        assertEquals(2, validator.validate(user).size(),
                "Некорректное количество ошибок валидации пустого пользователя");
    }

    @Test
    void addUserWrongEmail() {
        User user = new User();
        user.setEmail("jhbdfhgbehbkgjhbya.ru");
        user.setLogin("login_jhdsjkfbhbh");
        user.setName("name_njkfsdnif");
        user.setBirthday(LocalDate.now().minusYears(20));
        assertEquals(1, validator.validate(user).size(),
                "Ошибка валидации пользователя с некорректным email адресом");
    }

    @Test
    void addUserWrongBirthdate() {
        User user = new User();
        user.setEmail("jhbdfhgbehbkgjhb@ya.ru");
        user.setLogin("login_jhdsjkfbhbh");
        user.setName("name_njkfsdnif");
        user.setBirthday(LocalDate.now().plusYears(20));
        assertEquals(1, validator.validate(user).size(),
                "Ошибка валидации пользователя с некорректной датой рождения");
    }
}