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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPaymentMethod extends Fragment {

    private EditText cardHolderName;
    private EditText cardNumber;
    private EditText cardCVV;
    private EditText validationPeriod;
    private EditText phoneNumber;
    private Button addPaymentMethodButton;
    private TextView cancelAddPayment;

    private PaymentMethods paymentMethods;
    private ProgressDialog dialog;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference().child("Payment_Methods");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_payment_method, container, false);

        cardHolderName = view.findViewById(R.id.add_payment_name);
        cardNumber = view.findViewById(R.id.add_payment_card_number);
        cardCVV = view.findViewById(R.id.add_payment_cvv);
        validationPeriod = view.findViewById(R.id.add_payment_validation_period);
        phoneNumber = view.findViewById(R.id.add_payment_phone_number);
        addPaymentMethodButton = view.findViewById(R.id.add_payment_button);
        cancelAddPayment = view.findViewById(R.id.add_payment_cancel);

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(getContext());
        paymentMethods = new PaymentMethods();

        addPaymentMethod();

        return view;
    }

    private void addPaymentMethod() {

        addPaymentMethodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String holderName = cardHolderName.getText().toString().trim();
                String cardNum = cardNumber.getText().toString().trim();
                String cvv = cardCVV.getText().toString().trim();
                String validation = validationPeriod.getText().toString().trim();
                String phoneNum = phoneNumber.getText().toString().trim();
                String paymentMethodId = reference.push().getKey();

                if (TextUtils.isEmpty(holderName)) {
                    cardHolderName.setError("Card Holder name is required");
                    return;
                }
                if (TextUtils.isEmpty(cardNum)) {
                    cardNumber.setError("Card number is required");
                    return;
                }
                if (TextUtils.isEmpty(cvv)) {
                    cardCVV.setError("CVV is required");
                    return;
                }
                if (TextUtils.isEmpty(validation)) {
                    validationPeriod.setError("Validation period is required");
                    return;
                }
                if (TextUtils.isEmpty(phoneNum)) {
                    phoneNumber.setError("Phone number is required");
                    return;
                }

                dialog.setMessage("Adding Payment Method");
                dialog.show();

                PaymentMethod payMethod = new PaymentMethod(paymentMethodId, holderName, cardNum, cvv, validation, phoneNum);
                reference.child(auth.getCurrentUser().getUid()).child(paymentMethodId).setValue(payMethod).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Payment Method Added", Toast.LENGTH_SHORT).show();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.user_main_frame, paymentMethods);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Payment Method Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}