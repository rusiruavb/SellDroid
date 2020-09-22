package com.example.selldroid_final;

import android.widget.EditText;

public class Seller {

    private String profileImage;
    private String sellerName;
    private String sellerEmail;
    private String shopName;
    private String shopAddress;
    private String phoneNumber;
    private String password;
    private String type;

    public Seller() {}

    public Seller(String sellerName, String sellerEmail, String shopName, String shopAddress, String phoneNumber, String password, String type, String profileImage) {
        this.sellerName = sellerName;
        this.sellerEmail = sellerEmail;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.type = type;
        this.profileImage = profileImage;
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

    public String getType() { return  type; }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(String type) {
        this.type = type;
    }
}
