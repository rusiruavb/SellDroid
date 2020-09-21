package com.example.selldroid_final;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaymentMethods extends Fragment {

    private RecyclerView paymentMethodRecycleView;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference().child("Payment_Methods");

    private FirebaseRecyclerOptions<PaymentMethod> options;
    private FirebaseRecyclerAdapter<PaymentMethod, PaymentMethodViewHolder> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_methods, container, false);

        auth = FirebaseAuth.getInstance();
        paymentMethodRecycleView = view.findViewById(R.id.payment_method_recycle_view);
        paymentMethodRecycleView.setHasFixedSize(true);
        paymentMethodRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        options = new FirebaseRecyclerOptions.Builder<PaymentMethod>().setQuery(reference.child(auth.getCurrentUser().getUid()), PaymentMethod.class).build();
        adapter = new FirebaseRecyclerAdapter<PaymentMethod, PaymentMethodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PaymentMethodViewHolder holder, int position, @NonNull PaymentMethod model) {
                holder.cardNumber.setText(model.getCardNumber());
                holder.cardHolderName.setText(model.getHolderName());
                holder.paymentUserPhoneNumber.setText(model.getPhoneNumber());
                holder.validationPeriod.setText(model.getValidationPeriod());

                holder.deletePaymentMethod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }

            @NonNull
            @Override
            public PaymentMethodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.single_payment_method_item, parent, false);
                return new PaymentMethodViewHolder(v);
            }
        };

        adapter.startListening();
        paymentMethodRecycleView.setAdapter(adapter);

        return view;
    }
}