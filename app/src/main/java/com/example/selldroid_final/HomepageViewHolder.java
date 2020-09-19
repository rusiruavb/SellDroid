package com.example.selldroid_final;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomepageViewHolder extends RecyclerView.ViewHolder {

    public TextView itemName;
    public TextView itemPrice;
    public TextView itemQuantity;
    public ImageView itemImage;

    public HomepageViewHolder(@NonNull View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.display_item_name);
        itemPrice = itemView.findViewById(R.id.display_item_price);
        itemQuantity = itemView.findViewById(R.id.display_item_quantity);
        itemImage = itemView.findViewById(R.id.display_seller_product_image);
    }
}
