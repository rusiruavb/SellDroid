package com.example.selldroid_final;

public class User {
    private String name;
    private String email;
    private String phoneNumber;
    private String passowrd;
    private String profileImage;

    public User() {}

    public User(String name, String email, String phoneNumber, String passowrd, String profileImage) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passowrd = passowrd;
        this.profileImage = profileImage;
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

    public String getProfileImage() { return profileImage; }
}
