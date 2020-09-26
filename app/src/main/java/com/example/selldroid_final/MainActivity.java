package com.example.selldroid_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

public class MainActivity extends AppCompatActivity {

    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginButton;
    private TextView forgetPassword;
    private TextView createAccount;
    private TextView loginSeller;
    private ProgressDialog progress;

    // Firebase variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userRef = database.getReference().child("Users");
    private DatabaseReference sellerRef = database.getReference().child("Seller");

    // Fragments
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mFragment = new UserProfile();
        // check user already logged in

//        if (mAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), SellerHome.class));
////            sellerRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
////                @Override
////                public void onDataChange(@NonNull DataSnapshot snapshot) {
////                    String user = snapshot.child("type").getValue().toString();
////                    System.out.println(user);
//////
//////                    if (user.equals("seller")) {
//////                        startActivity(new Intent(getApplicationContext(), SellerHome.class));
//////                    } else {
//////                        startActivity(new Intent(getApplicationContext(), UserHome.class));
//////                    }
////                }
////
////                @Override
////                public void onCancelled(@NonNull DatabaseError error) { }
////            });
//        }

        userLogin();
    }

    private void userLogin() {
        loginEmail = findViewById(R.id.login_seller_email);
        loginPassword = findViewById(R.id.login_seller_password);
        loginButton = findViewById(R.id.login_seller_button);
        forgetPassword = findViewById(R.id.seller_foget_password);
        createAccount = findViewById(R.id.create_seller_account);
        loginSeller = findViewById(R.id.login_as_seller);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                // validation
                if (TextUtils.isEmpty(email)) {
                    loginEmail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    loginPassword.setError("Password is Required");
                    return;
                }

                progress.setMessage("Login in...");
                progress.show();

                // database connection and check user
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), UserHome.class));
//                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                            transaction.replace(R.id.main_frame,mFragment);
//                            transaction.commit();
                        } else {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(),"Email or Password was Incorrect",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserRegister.class));
            }
        });

        loginSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SellerLogin.class));
            }
        });
    }
}