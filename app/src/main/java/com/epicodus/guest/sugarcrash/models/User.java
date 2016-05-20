package com.epicodus.guest.sugarcrash.models;

import java.util.Calendar;

/**
 * Created by Guest on 5/16/16.
 */
public class User {
    private String name;
    private String email;
    private int birthYear;
    private String sex;


    public User() {}

    public User(String name, String email, int birthYear, String sex) {
        this.name = name;
        this.email = email;
        this.birthYear = birthYear;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getBirthYear() { return birthYear; }

    public String getSex() { return sex; }


    public int findAge() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return (year - birthYear);
    }
}

