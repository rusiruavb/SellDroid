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

public class UserRegister extends AppCompatActivity {

    // Layout variables
    private EditText userName;
    private EditText userEmail;
    private EditText userPhoneNumber;
    private EditText password1;
    private EditText password2;
    private Button registerButton;
    private TextView registerAsSeller;
    private TextView alreadyHaveAccount;
    private ProgressDialog dialog;

    // Firebase variables
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference collection = database.getReference().child("Users");
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        dialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        registerUser();
    }

    private void registerUser() {
        userName = findViewById(R.id.register_name);
        userEmail = findViewById(R.id.register_email);
        userPhoneNumber = findViewById(R.id.register_pNumber);
        password1 = findViewById(R.id.register_password);
        password2 = findViewById(R.id.register_conformPassword);
        registerButton = findViewById(R.id.register_button);
        registerAsSeller = findViewById(R.id.register_as_seller);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = userName.getText().toString().trim();
                final String email = userEmail.getText().toString().trim();
                final String phoneNumber = userPhoneNumber.getText().toString().trim();
                final String password = password1.getText().toString().trim();
                final String userImage = "https://firebasestorage.googleapis.com/v0/b/selldroid-final.appspot.com/o/Seller_Profile_images%2Fdefault_profile_image.png?alt=media&token=9c3e6a6e-04da-47bf-a36b-3a53ca7ae14c";
                String passwordTwo = password2.getText().toString().trim();

                // validation
                if (TextUtils.isEmpty(name)) {
                    userName.setError("Name is Required");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    userEmail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber)) {
                    userPhoneNumber.setError("Phone Number is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    password1.setError("Password is Required");
                    return;
                }
                if (TextUtils.isEmpty(passwordTwo)) {
                    password2.setError("Password is Required");
                    return;
                }
                if (!TextUtils.equals(password, passwordTwo)) {
                    password2.setError("Passwords Not Match");
                    return;
                }

                dialog.setMessage("Sending Data...");
                dialog.show();

                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Registration Success",Toast.LENGTH_LONG).show();
                            userId = auth.getCurrentUser().getUid();
                            User user = new User(name,email,phoneNumber,password,userImage);
                            collection.child(userId).setValue(user);
                            startActivity(new Intent(getApplicationContext(), UserHome.class));
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        registerAsSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SellerRegister.class));
            }
        });

    }
}