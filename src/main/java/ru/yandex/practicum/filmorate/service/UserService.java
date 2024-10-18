package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User get(Long id) {
        return userStorage.get(id);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User addFriend(Long id, Long friendId) {

        if (id.equals(friendId)) {
            throw new ValidationException("Нельзя добавить пользователя в друзья к самому себе");
        }

        User user = userStorage.get(id);
        User friend = userStorage.get(friendId);
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());

        return friend;
    }

    public User deleteFriend(Long id, Long friendId) {

        User user = userStorage.get(id);
        User friend = userStorage.get(friendId);
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());

        return friend;

    }

    public List<User> getFriends(Long id) {

        User user = userStorage.get(id);
        return user.getFriends().stream()
                .map(userStorage::get)
                .toList();

    }

    public List<User> getCommon(Long id, Long friendId) {

        return getFriends(id).stream()
                .filter(user -> getFriends(friendId).contains(user))
                .toList();

    }
}
