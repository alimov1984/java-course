package ru.alimov.application.user;

import java.util.List;

public interface UserService {

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(long userId);

    void deleteAllUsers();

    User getUserById(long id);

    User getByUserName(String userName);

    List<User> getAllUsers();
}
