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

public class SetNewSellerPassword extends AppCompatActivity {

    private EditText sellerEmail;
    private EditText phoneNumber;
    private Button changePasswordButton;
    private TextView cancel;
    private ProgressDialog dialog;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_seller_password);

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

        sellerEmail = findViewById(R.id.seller_forgot_email);
        phoneNumber = findViewById(R.id.seller_forgot_phone_number);
        changePasswordButton = findViewById(R.id.seller_forgot_button);
        cancel = findViewById(R.id.seller_forgot_cancel);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String seller_email = sellerEmail.getText().toString().trim();
                String seller_phoneNumber = phoneNumber.getText().toString().trim();


                if (TextUtils.isEmpty(seller_email)) {
                    sellerEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(seller_phoneNumber)) {
                    phoneNumber.setError("Phone number is required");
                    return;
                }

                dialog.setMessage("Connecting to Server...");
                dialog.show();

                auth.sendPasswordResetEmail(seller_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Check Your Gmail", Toast.LENGTH_LONG).show();
                            Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                            startActivity(intent);
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });



    }












}
