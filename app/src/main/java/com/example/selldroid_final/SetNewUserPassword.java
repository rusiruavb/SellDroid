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
import com.google.firebase.auth.FirebaseAuth;

public class SetNewUserPassword extends AppCompatActivity {

    private EditText userEmail;
    private EditText userPhoneNumber;
    private Button forgotButton;
    private TextView forgotPasswordCancel;
    private ProgressDialog dialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_user_password);

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        userEmail = findViewById(R.id.forgot_email);
        userPhoneNumber = findViewById(R.id.forgot_phone_number);
        forgotButton = findViewById(R.id.forgot_button);
        forgotPasswordCancel = findViewById(R.id.forgot_cancel);

        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = userEmail.getText().toString().trim();
                String phoneNumber = userPhoneNumber.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    userEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber)) {
                    userPhoneNumber.setError("Phone number is required");
                    return;
                }

                dialog.setMessage("Connecting to Server...");
                dialog.show();

                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
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