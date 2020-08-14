package com.example.selldroid_final;

public class User {
    private String name;
    private String email;
    private String phoneNumber;
    private String passowrd;

    public User() {}

    public User(String name, String email, String phoneNumber, String passowrd) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passowrd = passowrd;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassowrd() {
        return passowrd;
    }
}
