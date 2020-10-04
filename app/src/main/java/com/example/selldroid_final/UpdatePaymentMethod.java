package com.example.selldroid_final;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class UpdatePaymentMethod extends Fragment {


    private EditText cardNumber;
    private EditText cardHolderName;
    private EditText cardCVV;
    private EditText cardValidationPeriod;
    private Button updatePaymentButton;
    private TextView cancelCardUpdate;
    private String cardId;

    private FirebaseAuth auth;
    private FirebaseDatabase paymentMethodDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference paymentReference = paymentMethodDatabase.getReference().child("Payment_Methods");
    private ProgressDialog dialog;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_payment_method, container, false);


        cardHolderName = view.findViewById(R.id.update_payment_method_name);
        cardNumber = view.findViewById(R.id.update_payment_method_card_number);
        cardCVV = view.findViewById(R.id.update_payment_method_cvv);
        cardValidationPeriod = view.findViewById(R.id.update_payment_method_validation_period);
        updatePaymentButton = view.findViewById(R.id.update_payment_button);
        cancelCardUpdate = view.findViewById(R.id.cancel_update_payment);

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(getContext());
        bundle = getArguments();

        displayPaymentMethodDetails();

        updatePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCardDetails();
            }
        });
        cancelCardUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.user_main_frame, new PaymentMethods());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    private void displayPaymentMethodDetails() {
        cardHolderName.setText(bundle.getString("holderName"));
        cardNumber.setText(bundle.getString("cardNumber"));
        cardCVV.setText(bundle.getString("cvv"));
        cardValidationPeriod.setText(bundle.getString("validationPeriod"));
    }

    private void updateCardDetails() {
        String number = cardNumber.getText().toString().trim();
        String holderName = cardHolderName.getText().toString().trim();
        String validation = cardValidationPeriod.getText().toString().trim();
        String cvv = cardCVV.getText().toString().trim();
        String phone = bundle.getString("phoneNumber");
        String cardId = bundle.getString("paymentMethodId");

        if (TextUtils.isEmpty(number)) {
            cardNumber.setError("Card number is required");
            return;
        }
        if (TextUtils.isEmpty(holderName)) {
            cardHolderName.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(validation)) {
            cardValidationPeriod.setError("Date is required");
            return;
        }
        if (TextUtils.isEmpty(cvv)) {
            cardCVV.setError("CVV is required");
            return;
        }

        dialog.setMessage("Updating Payment Method...");
        dialog.show();

        PaymentMethod updatePayment = new PaymentMethod(cardId, holderName, number, cvv, validation, phone);

        paymentReference.child(auth.getCurrentUser().getUid()).setValue(updatePayment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Payment Updated", Toast.LENGTH_LONG).show();

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.user_main_frame, new PaymentMethods());
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Payment Update Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}