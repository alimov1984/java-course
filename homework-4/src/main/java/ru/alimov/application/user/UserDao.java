package ru.alimov.application.user;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    void insert(User user) throws SQLException ;

    void update(User user) throws SQLException ;

    void delete(long id) throws SQLException ;

    void deleteAll() throws SQLException ;

    List<User> getAll() throws SQLException ;

    User getById(long userId) throws SQLException;

    User getByUserName(String userName) throws SQLException;

}
