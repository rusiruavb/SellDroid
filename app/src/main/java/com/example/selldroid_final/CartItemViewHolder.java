package com.example.selldroid_final;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartItemViewHolder extends RecyclerView.ViewHolder {

    public ImageView cartItemImage;
    public TextView cartItemName;
    public TextView cartItemPrice;
    public Button cartItemDeleteButton;
    public Button addItemButton;
    public Button removeItemButton;
    public TextView cartItemQuantity;

    public CartItemViewHolder(@NonNull View itemView) {
        super(itemView);
        cartItemImage = itemView.findViewById(R.id.display_cart_product_image);
        cartItemName = itemView.findViewById(R.id.single_payment_method_number);
        cartItemPrice = itemView.findViewById(R.id.single_payment_item_name);
        cartItemDeleteButton = itemView.findViewById(R.id.cart_item_delete_button);
        addItemButton = itemView.findViewById(R.id.cart_item_add_button);
        removeItemButton = itemView.findViewById(R.id.cart_item_remove_button);
        cartItemQuantity = itemView.findViewById(R.id.cart_item_quantity);
    }
}
