package com.example.selldroid_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

public class MainActivity extends AppCompatActivity {

    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginButton;
    private TextView forgetPassword;
    private TextView createAccount;
    private ProgressDialog progress;

    // Firebase variables
    private FirebaseAuth mAuth;
    Fragment fragment = new UserProfile();

    // Fragments
    private Fragment mFragment = new UserProfile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_seller_items);
        progress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        // check user already logged in
//        if (mAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), UserHome.class));
//        }
       // userLogin();
    }

//    private void userLogin() {
//        loginEmail = findViewById(R.id.login_email);
//        loginPassword = findViewById(R.id.login_password);
//        loginButton = findViewById(R.id.login_button);
//        forgetPassword = findViewById(R.id.foget_password);
//        createAccount = findViewById(R.id.create_account);
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email = loginEmail.getText().toString().trim();
//                String password = loginPassword.getText().toString().trim();
//
//                // validation
//                if (TextUtils.isEmpty(email)) {
//                    loginEmail.setError("Email is Required");
//                    return;
//                }
//                if (TextUtils.isEmpty(password)) {
//                    loginPassword.setError("Password is Required");
//                    return;
//                }
//
//                progress.setMessage("Login in...");
//                progress.show();
//
//                // database connection and check user
//                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            progress.dismiss();
//                            Toast.makeText(getApplicationContext(),"Login Susccessful",Toast.LENGTH_SHORT).show();
//                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                            transaction.replace(R.id.main_frame,mFragment);
//                            transaction.commit();
//                        } else {
//                            progress.dismiss();
//                            Toast.makeText(getApplicationContext(),"Email or Password was Incorrect",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });
//
//        createAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), UserRegister.class));
//            }
//        });
//    }
}