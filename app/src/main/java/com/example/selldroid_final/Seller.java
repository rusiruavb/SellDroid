package com.example.selldroid_final;

import android.widget.EditText;

public class Seller {

    private String sellerName;
    private String sellerEmail;
    private String shopName;
    private String shopAddress;
    private String phoneNumber;
    private String password;
    private String confirmPassword;

    public Seller(String sellerName, String sellerEmail, String shopName, String shopAddress, String phoneNumber, String password, String confirmPassword) {
        this.sellerName = sellerName;
        this.sellerEmail = sellerEmail;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
