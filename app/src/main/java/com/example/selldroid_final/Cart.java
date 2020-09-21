package com.example.selldroid_final;

public class Cart {
    private String cartId;
    private String itemName;
    private String itemPrice;
    private String itemImage;
    private int itemQuantity;
    private int totalPrice;

    public Cart() {}

    public Cart(String cartId, String itemName, String itemPrice, String itemImage, int itemQuantity, int totalPrice) {
        this.cartId = cartId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemImage = itemImage;
        this.itemQuantity = itemQuantity;
        this.totalPrice = totalPrice;
    }

    public String getCartId() {
        return cartId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemImage() {
        return itemImage;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
