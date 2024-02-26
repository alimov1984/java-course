package ru.alimov.application.user;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void addUser(User user) {
        try {
            userDao.insert(user);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            userDao.update(user);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteUser(long userId) {
        try {
            userDao.delete(userId);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteAllUsers() {
        try {
            userDao.deleteAll();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public User getUserById(long id) {
        User user = null;
        try {
            user = userDao.getById(id);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return user;
    }

    @Override
    public User getByUserName(String userName) {
        User user = null;
        try {
            user = userDao.getByUserName(userName);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return user;
    }

    public List<User> getAllUsers() {
        List<User> userList = null;
        try {
            userList = userDao.getAll();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return userList;
    }
}
