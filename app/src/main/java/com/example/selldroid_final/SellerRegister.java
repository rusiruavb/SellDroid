package com.example.selldroid_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellerRegister extends AppCompatActivity {

    private EditText sellerName;
    private EditText sellerEmail;
    private EditText shopName;
    private EditText shopAddress;
    private EditText phoneNumber;
    private EditText password;
    private EditText confirmPassword;
    private Button createNewAccButton;
    private TextView hasAccount;
    private ProgressDialog dialog;

    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference collection = mFirebaseDatabase.getReference().child("Seller");
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);
        dialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        registerSeller();
    }

    private void registerSeller() {
        sellerName = findViewById(R.id.seller_register_name);
        sellerEmail = findViewById(R.id.seller_register_email);
        shopName = findViewById(R.id.seller_shop_name);
        shopAddress = findViewById(R.id.seller_shop_address);
        phoneNumber = findViewById(R.id.seller_register_pNumber);
        password = findViewById(R.id.seller_register_password);
        confirmPassword = findViewById(R.id.seller_register_conformPassword);
        createNewAccButton = findViewById(R.id.seller_register_button);
        hasAccount = findViewById(R.id.seller_register_to_login);


        createNewAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String seller_name = sellerName.getText().toString().trim();
                final String seller_email = sellerEmail.getText().toString().trim();
                final String shop_name = shopName.getText().toString().trim();
                final String shop_address = shopAddress.getText().toString().trim();
                final String phone_number = phoneNumber.getText().toString().trim();
                final String pass_word = password.getText().toString().trim();
                final String confirm_password = confirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(seller_name)) {
                    sellerName.setError("Please enter seller name");
                    return;
                }
                if (TextUtils.isEmpty(seller_email)) {
                    sellerEmail.setError("Please enter email");
                    return;
                }
                if (TextUtils.isEmpty(shop_name)) {
                    shopName.setError("Please enter shop name");
                    return;
                }
                if (TextUtils.isEmpty(shop_address)) {
                    shopAddress.setError("Please enter shop address");
                    return;
                }
                if (TextUtils.isEmpty(phone_number)) {
                    phoneNumber.setError("Please enter phone number");
                    return;
                }
                if (TextUtils.isEmpty(pass_word)) {
                    password.setError("Please enter the password");
                    return;
                }
                if (TextUtils.isEmpty(confirm_password)) {
                    confirmPassword.setError("Please reenter the password");
                    return;

                }

                dialog.setMessage("Sending Data...");
                dialog.show();

                auth.createUserWithEmailAndPassword(seller_email, pass_word).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Registration Success", Toast.LENGTH_LONG).show();
                            userId = auth.getCurrentUser().getUid();
                            Seller seller = new Seller(seller_name, seller_email, shop_name, shop_address, phone_number, pass_word);
                            collection.child(userId).setValue(seller);
                            startActivity(new Intent(getApplicationContext(), SellerHome.class));
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

            }
        });

    }
}

