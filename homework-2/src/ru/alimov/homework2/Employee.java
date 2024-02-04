package ru.alimov.homework2;

public class Employee {
    private String fio;
    private short age;
    private String position;

    public Employee(String fio, short age, String position) {
        this.fio = fio;
        this.age = age;
        this.position = position;
    }

    public String getFio() {
        return this.fio;
    }

    public String getPosition() {
        return this.position;
    }

    public short getAge() {
        return age;
    }
}
