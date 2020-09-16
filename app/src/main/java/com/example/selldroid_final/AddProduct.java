package com.example.selldroid_final;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProduct extends Fragment {

    private EditText productName;
    private EditText price;
    private EditText quantity;
    private Button addButton;
    private ProgressDialog mProgressDialog;
    private String userId;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference = mFirebaseDatabase.getReference().child("Items");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        productName = view.findViewById(R.id.add_product_name);
        price = view.findViewById(R.id.add_product_price);
        quantity = view.findViewById(R.id.add_product_quantity);
        addButton = view.findViewById(R.id.add_product_button);
        mAuth = FirebaseAuth.getInstance();
        sendItem();
        return view;
    }

    private void sendItem() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String product_name = productName.getText().toString().trim();
                final String product_price = price.getText().toString().trim();
                final String product_quantity = quantity.getText().toString().trim();

                if (TextUtils.isEmpty(product_name)) {
                    productName.setError("Please fill product name");
                }
                if (TextUtils.isEmpty(product_price)) {
                    price.setError("Please enter price");
                }
                if (TextUtils.isEmpty(product_quantity)) {
                    quantity.setError("Please enter quantity");
                }

//                mProgressDialog.setMessage("Adding Item...");
//                mProgressDialog.show();

                userId = mAuth.getCurrentUser().getUid();
                Item i = new Item(product_name, product_price, product_quantity);
                mReference.child(userId).setValue(i).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
//                            mProgressDialog.dismiss();
                            Toast.makeText(getContext(),"Success", Toast.LENGTH_SHORT).show();
                        } else {
//                            mProgressDialog.dismiss();
                            Toast.makeText(getContext(),"Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}