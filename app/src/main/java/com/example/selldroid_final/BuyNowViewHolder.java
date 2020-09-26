package com.example.selldroid_final;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class BuyNowViewHolder extends RecyclerView.ViewHolder {

    public TextView cardNumber;
    public TextView cartHolderName;
    public TextView cardValidation;
    public TextView cardPhoneNumber;
    public ConstraintLayout paymentMethod;

    public BuyNowViewHolder(@NonNull View itemView) {
        super(itemView);
        cardNumber = itemView.findViewById(R.id.payment_method_number);
        cartHolderName  =itemView.findViewById(R.id.payment_item_name);
        cardValidation = itemView.findViewById(R.id.payment_item_validation_date);
        cardPhoneNumber = itemView.findViewById(R.id.payment_item_phone_number);
        paymentMethod = itemView.findViewById(R.id.payment_item);
    }
}
