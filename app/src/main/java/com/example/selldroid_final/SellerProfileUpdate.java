package com.example.selldroid_final;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerProfileUpdate extends Fragment {

    private EditText updateSellerName;
    private EditText updateSellerEmail;
    private EditText updateSellerShopName;
    private EditText updateSellerShopAddress;
    private EditText updateSellerPhoneNumber;
    private Button updateSellerButton;
    private Button deleteSellerButton;
    private ProgressDialog dialog;

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference().child("Seller");
    private DatabaseReference seller;

    private String profileImage;
    private String type;
    private String password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_profile_update, container, false);

        updateSellerName = view.findViewById(R.id.update_seller_name);
        updateSellerEmail = view.findViewById(R.id.update_seller_email);
        updateSellerShopName = view.findViewById(R.id.update_seller_company);
        updateSellerShopAddress = view.findViewById(R.id.update_seller_address);
        updateSellerPhoneNumber = view.findViewById(R.id.update_seller_phoneNum);
        updateSellerButton = view.findViewById(R.id.update_seller_button);
        deleteSellerButton = view.findViewById(R.id.delete_seller_account);

        dialog = new ProgressDialog(getContext());
        auth = FirebaseAuth.getInstance();
        seller = reference.child(auth.getCurrentUser().getUid());
        firebaseUser = auth.getCurrentUser();

        displaySellerDetails();

        updateSellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSellerAccount();
            }
        });

        deleteSellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Delete Item");
                alertDialog.setMessage("Do you want to delete this item ?");
                alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //deleteSellerAccount();
                    }
                });
                alertDialog.setButton2("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        return view;
    }

    private void displaySellerDetails() {
        seller.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    updateSellerName.setText(snapshot.child("sellerName").getValue().toString());
                    updateSellerEmail.setText(snapshot.child("sellerEmail").getValue().toString());
                    updateSellerPhoneNumber.setText(snapshot.child("phoneNumber").getValue().toString());
                    updateSellerShopName.setText(snapshot.child("shopName").getValue().toString());
                    updateSellerShopAddress.setText(snapshot.child("shopAddress").getValue().toString());
                    profileImage = snapshot.child("profileImage").getValue().toString();
                    type = "seller";
                    password = snapshot.child("password").getValue().toString();
                } else {
                    startActivity(new Intent(getContext(), SellerRegister.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void updateSellerAccount() {
        dialog.setMessage("Updating seller profile");
        dialog.show();

        String name = updateSellerName.getText().toString().trim();
        String email = updateSellerEmail.getText().toString().trim();
        String shopName = updateSellerShopName.getText().toString().trim();
        String phone = updateSellerPhoneNumber.getText().toString().trim();
        String address = updateSellerShopAddress.getText().toString().trim();

        Seller updatedSeller = new Seller(name, email, shopName, address, phone, password, type, profileImage);

        seller.setValue(updatedSeller).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Update Success", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // last day I stopped here
    // need to finish the deleteSellerAccount method
    // need to complete the user profile
    // need to complete the buy now function
    // run overall app using nithya's lap and my lap. one as seller and one as user

    private void deleteSellerAccount() {
        dialog.setMessage("Deleting User...");
        dialog.show();

        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    seller.removeValue();

                    dialog.dismiss();
                    Toast.makeText(getContext(), "Your Seller Account Deleted", Toast.LENGTH_SHORT).show();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.remove(new SellerProfile()).commit();
//                    FragmentTransaction transaction1 = getActivity().getSupportFragmentManager().beginTransaction();
//                    transaction1.remove(new SellerProfileUpdate()).commit();
                    startActivity(new Intent(getContext(), SellerRegister.class));
                } else {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Deleting Account Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}