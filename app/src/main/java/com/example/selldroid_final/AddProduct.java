package com.example.selldroid_final;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddProduct extends Fragment {

    private EditText productName;
    private EditText price;
    private EditText quantity;
    private Button addButton;
    private ProgressDialog mProgressDialog;
    private String userId;
    private long maxCount;
    private long allItemsMaxCount;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference = mFirebaseDatabase.getReference().child("Seller_Items");
    private DatabaseReference allItems = mFirebaseDatabase.getReference().child("All_Items");

    private AddProductImage productImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        productName = view.findViewById(R.id.add_product_name);
        price = view.findViewById(R.id.add_product_price);
        quantity = view.findViewById(R.id.add_product_quantity);
        addButton = view.findViewById(R.id.add_product_button);
        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(getActivity());
        productImage = new AddProductImage();
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
                    return;
                }
                if (TextUtils.isEmpty(product_price)) {
                    price.setError("Please enter price");
                    return;
                }
                if (TextUtils.isEmpty(product_quantity)) {
                    quantity.setError("Please enter quantity");
                    return;
                }

                mProgressDialog.setMessage("Adding Item...");
                mProgressDialog.show();

                userId = mAuth.getCurrentUser().getUid();
                mReference.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        maxCount = snapshot.getChildrenCount();
                        allItemsMaxCount = snapshot.getChildrenCount();
                        System.out.println("Items " + maxCount);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                final Item i = new Item(product_name, product_price, product_quantity);
                mReference.child(userId).child(String.valueOf(maxCount)).setValue(i).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mProgressDialog.dismiss();
                            allItems.child(String.valueOf(allItemsMaxCount)).setValue(i);
                            Toast.makeText(getContext(),"Success", Toast.LENGTH_SHORT).show();
                            // change fragment to add product image
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.main_frame, productImage);
                            transaction.commit();
                        } else {
                            mProgressDialog.dismiss();
                            Toast.makeText(getContext(),"Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}