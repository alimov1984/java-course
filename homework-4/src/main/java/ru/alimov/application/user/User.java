package ru.alimov.application.user;

public class User {
    private Long id;
    private String userName;

    public User() {
    }

    public User(String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return String.format("{id=%s, userName=%s}\n", this.id, this.userName);
    }

}
