package com.example.selldroid_final;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class HomepageViewHolder extends RecyclerView.ViewHolder {

    public TextView itemName;
    public TextView itemPrice;
    public TextView itemQuantity;
    public ImageView itemImage;
    public CardView itemCard;

    public HomepageViewHolder(@NonNull View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.display_item_name);
        itemPrice = itemView.findViewById(R.id.display_item_price);
        itemQuantity = itemView.findViewById(R.id.display_item_quantity);
        itemImage = itemView.findViewById(R.id.display_cart_product_image);
        itemCard = itemView.findViewById(R.id.item_card);
    }
}
