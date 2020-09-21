package com.example.selldroid_final;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PaymentMethodViewHolder extends RecyclerView.ViewHolder {

    public TextView cardNumber;
    public TextView cardHolderName;
    public TextView paymentUserPhoneNumber;
    public TextView validationPeriod;
    public Button deletePaymentMethod;

    public PaymentMethodViewHolder(@NonNull View itemView) {
        super(itemView);
        cardNumber = itemView.findViewById(R.id.display_payment_number);
        cardHolderName = itemView.findViewById(R.id.display_payment_holder_name);
        paymentUserPhoneNumber = itemView.findViewById(R.id.display_payment_phone_number);
        validationPeriod = itemView.findViewById(R.id.display_payment_validation_period);
        deletePaymentMethod = itemView.findViewById(R.id.payment_item_delete_button);
    }
}
