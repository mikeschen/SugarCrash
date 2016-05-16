package com.example.guest.sugarcrash.models;

/**
 * Created by Guest on 5/16/16.
 */
public class User {
    private String name;
    private String email;
    private String age;

    public User() {}

    public User(String name, String email, String age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAge() { return age; }
}

