package com.example.selldroid_final;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ConformedPaymentViewHolder extends RecyclerView.ViewHolder {

    public TextView paymentId;
    public TextView paymentDate;
    public TextView totalPrice;

    public ConformedPaymentViewHolder(@NonNull View itemView) {
        super(itemView);
        paymentId = itemView.findViewById(R.id.payment_id);
        paymentDate = itemView.findViewById(R.id.payment_date);
        totalPrice = itemView.findViewById(R.id.total_price);
    }
}
