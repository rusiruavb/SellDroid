package com.example.selldroid_final;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class HomepageViewHolder extends RecyclerView.ViewHolder {

    public TextView itemName;
    public TextView itemPrice;
    public TextView itemQuantity;
    public ImageView itemImage;
    public CardView itemCard;

    public HomepageViewHolder(@NonNull View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.seller_profile_item_name);
        itemPrice = itemView.findViewById(R.id.display_payment_holder_name);
        itemQuantity = itemView.findViewById(R.id.display_payment_phone_number);
        itemImage = itemView.findViewById(R.id.display_cart_product_image);
        itemCard = itemView.findViewById(R.id.item_card);
    }
}
