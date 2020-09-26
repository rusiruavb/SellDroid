package com.example.selldroid_final;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class UserProfile extends Fragment {

    private ImageView userProfile;
    private TextView userName;
    private TextView userEmail;
    private Button userLogoutButton;
    private static final int GALARY_INTENT = 2;
    private Uri imageUri;
    private ProgressDialog dialog;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference().child("Users");
    private DatabaseReference conformedPaymentReference = database.getReference().child("User_Payments");
    private StorageReference imageReference;

    private RecyclerView purchasedDetailsRecyclerView;
    private FirebaseRecyclerOptions<ConformedPayment> options;
    private FirebaseRecyclerAdapter<ConformedPayment, ConformedPaymentViewHolder> adapter;

    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private String image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        //userLogoutButton = view.findViewById(R.id.user_logout_button);
        purchasedDetailsRecyclerView = view.findViewById(R.id.purchased_items_recycler_view);
        purchasedDetailsRecyclerView.setHasFixedSize(true);
        purchasedDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userProfile = view.findViewById(R.id.user_image);
        userName = view.findViewById(R.id.user_profile_name);
        userEmail = view.findViewById(R.id.user_profile_email);
        auth = FirebaseAuth.getInstance();
        imageReference = FirebaseStorage.getInstance().getReference("User_Profile_images");
        dialog = new ProgressDialog(getContext());
        //userLogout();

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "SellerProfileImage"), GALARY_INTENT);
            }
        });
        displayUserData();

        options = new FirebaseRecyclerOptions.Builder<ConformedPayment>().setQuery(conformedPaymentReference.child(auth.getCurrentUser().getUid()), ConformedPayment.class).build();
        adapter = new FirebaseRecyclerAdapter<ConformedPayment, ConformedPaymentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ConformedPaymentViewHolder holder, int position, @NonNull ConformedPayment model) {
                holder.paymentId.setText(model.getPaymentId());
                holder.paymentDate.setText(model.getDate());
                holder.totalPrice.setText(model.getTotalPrice());
            }

            @NonNull
            @Override
            public ConformedPaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.single_purchased_item, parent, false);
                return new ConformedPaymentViewHolder(v);
            }
        };

        adapter.startListening();
        purchasedDetailsRecyclerView.setAdapter(adapter);

        return view;
    }

    private void displayUserData() {
        reference.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.child("profileImage").getValue().toString()).into(userProfile);
                userName.setText(snapshot.child("name").getValue().toString());
                userEmail.setText(snapshot.child("email").getValue().toString());

                name = snapshot.child("name").getValue().toString();
                email = snapshot.child("email").getValue().toString();
                password = snapshot.child("passowrd").getValue().toString();
                phoneNumber = snapshot.child("phoneNumber").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALARY_INTENT && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                userProfile.setImageBitmap(bitmap);

                dialog.setMessage("Adding image...");
                dialog.show();

                final StorageReference imageRef = imageReference.child(System.currentTimeMillis() + "." + GetFileExtension(imageUri));
                imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                User updateUser = new User(name, email, phoneNumber, password, url);
                                reference.child(auth.getCurrentUser().getUid()).setValue(updateUser);
                                Toast.makeText(getContext(), "Image added", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
//
//    private void userLogout() {
//        userLogoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                auth.signOut();
//                Toast.makeText(getContext(), "You are now signout", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getContext(), MainActivity.class));
//            }
//        });
//    }
}