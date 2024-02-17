package ru.alimov.userapplication;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Runner {
    public static void main(String[] args) {


        ApplicationContext ac = new AnnotationConfigApplicationContext("ru.alimov.userapplication");
        UserDao userDao = ac.getBean(UserDao.class);
        userDao.printme();
    }
}
