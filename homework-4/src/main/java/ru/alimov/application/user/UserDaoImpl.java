package ru.alimov.application.user;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

@Repository
public class UserDao {
    private final DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(User user) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement("INSERT INTO Users (username) VALUES (?)")) {
            pst.setString(1, user.getUserName());
            pst.executeUpdate();
        }
    }

    public void update(User user) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement("UPDATE Users SET username=? WHERE id=?")) {
            pst.setString(1, user.getUserName());
            pst.setLong(2, user.getId());
            pst.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement("DELETE FROM Users WHERE id=?")) {
            pst.setLong(1, id);
            pst.executeUpdate();
        }
    }

    public void deleteAll() throws SQLException {
        try (Connection con = dataSource.getConnection();
             Statement st = con.createStatement()) {
            st.execute("DELETE FROM Users");
        }
    }

    public List<User> getAll() throws SQLException {
        List<User> userList = new LinkedList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT id, username FROM users");
             ResultSet rs = pst.executeQuery();) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong(1));
                user.setUserName(rs.getString(2));
                userList.add(user);
            }
        }
        return userList;
    }

    public User getById(long userId) throws SQLException {
        User user = null;
        ResultSet rs = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT id, username FROM users WHERE id=?");) {
            pst.setLong(1, userId);
            rs = pst.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getLong(1));
                user.setUserName(rs.getString(2));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return user;
    }
}
