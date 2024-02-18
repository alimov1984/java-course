package ru.alimov.application.user;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class Runner {
    public static void main(String[] args) {

        ApplicationContext ac = new AnnotationConfigApplicationContext("ru.alimov.application.user");

        UserService userService = ac.getBean(UserServiceImpl.class);

        userService.deleteAllUsers();
        List<User> userList = userService.getAllUsers();
        showList("Deleted all users in db:", userList);

        userList = new ArrayList<>();
        userList.add(new User("name1"));
        userList.add(new User("name2"));
        userList.add(new User("name3"));
        userList.add(new User("name4"));
        for (User user : userList) {
            try {
                userService.addUser(user);
            } catch (Exception ex) {
                System.out.println((ex));
            }
        }
        userList = userService.getAllUsers();
        showList("Added users", userList);

        User user = userService.getByUserName("name2");
        if (user != null) {
            user.setUserName("name22");
            userService.updateUser(user);
        }
        userList = userService.getAllUsers();
        showList("Updated name2 user", userList);

        User user2 = userService.getByUserName("name3");
        if (user2 != null) {
            userService.deleteUser(user2.getId());
        }
        userList = userService.getAllUsers();
        showList("Deleted name3", userList);

        userService.deleteAllUsers();
        userList = userService.getAllUsers();
        showList("Deleted all users in db:", userList);
    }

    private static void showList(String message, List<User> userList) {
        System.out.printf("%s\nCurrent list of users:\n%s\n", message, userList);
    }
}
