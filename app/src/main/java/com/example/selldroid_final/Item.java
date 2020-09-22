package com.example.selldroid_final;

public class Item {
    private String itemId;
    private String name;
    private String price;
    private String quantity;
    private String imageUri;
    private String sellerName;
    private String sellerEmail;
    private String sellerPhoneNumber;
    private String shopName;
    private String shopAddress;
    private String type;
    private String description;

    public Item() {}

    public Item(String itemId, String name, String price, String quantity, String imageUri, String sellerName, String sellerEmail, String sellerPhoneNumber, String shopName, String shopAddress, String type, String description) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUri = imageUri;
        this.sellerName = sellerName;
        this.sellerEmail = sellerEmail;
        this.sellerPhoneNumber = sellerPhoneNumber;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.type = type;
        this.description = description;
    }

    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public String getSellerPhoneNumber() {
        return sellerPhoneNumber;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
