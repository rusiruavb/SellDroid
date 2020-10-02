package com.example.selldroid_final;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SellerHomeItemViewHolder extends RecyclerView.ViewHolder {

    public ImageView itemImage;
    public TextView itemName;
    public TextView itemPrice;
    public TextView itemQuantity;

    public SellerHomeItemViewHolder(@NonNull View itemView) {
        super(itemView);
        itemImage = itemView.findViewById(R.id.seller_home_product_image);
        itemName = itemView.findViewById(R.id.seller_home_item_name);
        itemPrice = itemView.findViewById(R.id.seller_home_item_price);
        itemQuantity = itemView.findViewById(R.id.seller_home_item_quantity);
    }
}
