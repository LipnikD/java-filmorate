package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;

import java.time.LocalDate;
import java.util.List;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    UserController userController;
    Validator validator;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
        validator = buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void addGoodUser() {
        User user = User.builder()
                .email("jhbdfhgbehbkgjhb@ya.ru")
                .login("login_jhdsjkfbhbh")
                .name("name_njkfsdnif")
                .birthday(LocalDate.now().minusYears(20))
                .build();
        userController.create(user);
        List<User> users = userController.findAll();
        assertEquals(1, users.size(), "Ошибка при сохранении пользователя.");
        assertEquals(user.getId(), users.getFirst().getId(), "Некорректный идентификатор сохраненного пользователя.");
    }

    @Test
    void updateUser() {
        User user = User.builder()
                .email("jhbdfhgbehbkgjhb@ya.ru")
                .login("login_jhdsjkfbhbh")
                .name("name_njkfsdnif")
                .birthday(LocalDate.now().minusYears(20))
                .build();
        userController.create(user);
        user.setName("name_updated");
        userController.update(user);
        List<User> users = userController.findAll();
        assertEquals(1, users.size(), "Ошибка при обновлении пользователя.");
    }

    @Test
    void addEmptyUser() {
        User user = User.builder()
                .build();
        assertEquals(2, validator.validate(user).size(),
                "Некорректное количество ошибок валидации пустого пользователя");
    }

    @Test
    void addUserWrongEmail() {
        User user = User.builder()
                .email("jhbdfhgbehbkgjhbya.ru")
                .login("login_jhdsjkfbhbh")
                .name("name_njkfsdnif")
                .birthday(LocalDate.now().minusYears(20))
                .build();
        assertEquals(1, validator.validate(user).size(),
                "Ошибка валидации пользователя с некорректным email адресом");
    }

    @Test
    void addUserWrongBirthdate() {
        User user = User.builder()
                .email("jhbdfhgbehbkgjhb@ya.ru")
                .login("login_jhdsjkfbhbh")
                .name("name_njkfsdnif")
                .birthday(LocalDate.now().plusYears(20))
                .build();
        assertEquals(1, validator.validate(user).size(),
                "Ошибка валидации пользователя с некорректной датой рождения");
    }
}