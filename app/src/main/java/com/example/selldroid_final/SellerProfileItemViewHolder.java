package com.example.selldroid_final;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SellerProfileItemViewHolder extends RecyclerView.ViewHolder {

    public TextView sellerProfileItemName;
    public TextView sellerProfileItemPrice;
    public TextView sellerProfileItemQuantity;

    public SellerProfileItemViewHolder(@NonNull View itemView) {
        super(itemView);
        sellerProfileItemName = itemView.findViewById(R.id.seller_profile_item_name);
        sellerProfileItemPrice = itemView.findViewById(R.id.seller_profile_item_price);
        sellerProfileItemQuantity = itemView.findViewById(R.id.seller_profile_item_quantity);
    }
}
