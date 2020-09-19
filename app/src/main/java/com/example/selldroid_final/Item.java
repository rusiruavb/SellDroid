package com.example.selldroid_final;

public class Item {
    private String name;
    private String price;
    private String quantity;
    private String imageUri;

    public Item() {}

    public Item(String name, String price, String quantity, String imageUri) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUri = imageUri;
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
}
