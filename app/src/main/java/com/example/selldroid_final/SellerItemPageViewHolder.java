package com.example.selldroid_final;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class SellerItemPageViewHolder extends RecyclerView.ViewHolder {

    public ImageView sellerItemImage;
    public TextView sellerItemName;
    public TextView sellerItemPrice;
    public TextView sellerItemQuantity;
    public CardView sellterItemCard;

    public SellerItemPageViewHolder(@NonNull final View itemView) {
        super(itemView);
        sellerItemImage = itemView.findViewById(R.id.display_cart_product_image);
        sellerItemName = itemView.findViewById(R.id.display_cart_item_name);
        sellerItemPrice = itemView.findViewById(R.id.display_cart_item_price);
        sellerItemQuantity = itemView.findViewById(R.id.display_seller_item_quantity);
        sellterItemCard = itemView.findViewById(R.id.seller_item_card);
    }
}
