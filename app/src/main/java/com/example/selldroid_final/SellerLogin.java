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

public class SellerLogin extends AppCompatActivity {

    private EditText sellerEmail;
    private EditText sellerPassword;
    private Button sellerLoginButton;
    private TextView sellerForgetPassword;
    private TextView sellerCreateAccount;
    private ProgressDialog dialog;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);
        sellerForgetPassword = findViewById(R.id.seller_forget_password);
        sellerCreateAccount =findViewById(R.id.create_seller_account);
        dialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        sellerForgetPassword = findViewById(R.id.seller_forget_password);
        dialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

//        if (auth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), SellerHome.class));
//        }

        sellerForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SetNewSellerPassword.class));
            }
        });
        sellerCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SellerRegister.class));
            }
        });




        sellerLogin();
    }

    private void sellerLogin() {
        sellerEmail = findViewById(R.id.login_seller_email);
        sellerPassword = findViewById(R.id.login_seller_password);
        sellerLoginButton = findViewById(R.id.login_seller_button);

        sellerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = sellerEmail.getText().toString().trim();
                String password = sellerPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    sellerEmail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    sellerPassword.setError("Password is Required");
                    return;
                }

                dialog.setMessage("Login in...");
                dialog.show();

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), SellerHome.class));
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Email or Password is wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}