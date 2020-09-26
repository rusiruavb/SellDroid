package com.example.selldroid_final;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileUpdate extends Fragment {

    private EditText updateUserName;
    private EditText updateUserPhoneNumber;
    private EditText updatePassword;
    private EditText updateConformPassword;
    private Button updateButton;
    private Button deleteButton;
    private TextView cancelUpdate;
    private TextView updateEmail;
    private ProgressDialog dialog;
    private String profileImageUrl;
    private String email;
    private String currentPassword;

    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userReference = database.getReference().child("Users");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_update, container, false);

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(getContext());
        updateEmail = view.findViewById(R.id.update_user_email);
        updateUserName = view.findViewById(R.id.update_user_name);
        updateUserPhoneNumber = view.findViewById(R.id.update_user_phoneNum);
        updatePassword = view.findViewById(R.id.update_user_password);
        updateConformPassword = view.findViewById(R.id.update_user_conform_password);
        updateButton = view.findViewById(R.id.update_user_button);
        deleteButton = view.findViewById(R.id.delete_user_account);
        cancelUpdate = view.findViewById(R.id.cancel_user_update);

        displayUserData();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser();
            }
        });

        return view;
    }

    private void displayUserData() {
        userReference.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updateUserName.setText(snapshot.child("name").getValue().toString());
                updateUserPhoneNumber.setText(snapshot.child("phoneNumber").getValue().toString());
                updateEmail.setText(snapshot.child("email").getValue().toString());
                profileImageUrl = snapshot.child("profileImage").getValue().toString();
                currentPassword = snapshot.child("passowrd").getValue().toString();
                email = snapshot.child("email").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void updateUser() {
        final String name = updateUserName.getText().toString().trim();
        final String password = updatePassword.getText().toString().trim();
        String conformPassword = updateConformPassword.getText().toString().trim();
        final String phone = updateUserPhoneNumber.getText().toString().trim();
        final String image = profileImageUrl;

        if (TextUtils.isEmpty(name)) {
            updateUserName.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            updateUserPhoneNumber.setError("Phone number is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            updatePassword.setError("Password is required");
            return;
        }
        if (TextUtils.isEmpty(conformPassword)) {
            updateConformPassword.setError("Please conform the password");
        }
        if (!TextUtils.equals(password, conformPassword)) {
            updateConformPassword.setError("Password is not matched");
            return;
        }

        dialog.setMessage("Updating Profile...");
        dialog.show();

        System.out.println(email);
        System.out.println(currentPassword);

        AuthCredential credential = EmailAuthProvider.getCredential(email, currentPassword);

        System.out.println(auth.getCurrentUser());

        auth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    auth.getCurrentUser().updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                User updateUser = new User(name, email, phone, password, image);
                                userReference.child(auth.getCurrentUser().getUid()).setValue(updateUser);
                                Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();

                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.main_frame, new UserProfile());
                                transaction.addToBackStack(null);
                                transaction.commit();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Profile Update Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Re-authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteUser() {

        dialog.setMessage("Deleting Profile...");
        dialog.show();

        auth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Profile Deleted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), UserRegister.class));
                } else {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Profile Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}