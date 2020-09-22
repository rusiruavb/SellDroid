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

public class SellerProfile extends Fragment {

    private Button sellerLogoutButton;
    private ImageView sellerProfile;
    private TextView displaySellerName;
    private TextView displaySellerCompany;
    private TextView displaySellerPhoneNumber;
    private RecyclerView sellingItemsRecycleView;
    private static final int GALARY_INTENT = 2;
    private Uri imageUri;
    private ProgressDialog dialog;

    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private StorageReference profileStoreage;
    private DatabaseReference reference = database.getReference().child("Seller");
    private DatabaseReference sellerItemReference = database.getReference().child("Seller_Items");
    private DatabaseReference sellerItem;

    private String sellerName;
    private String sellerEmail;
    private String sellerPhoneNumber;
    private String shopName;
    private String address;
    private String password;
    private String type;

    private FirebaseRecyclerOptions<Item> options;
    private FirebaseRecyclerAdapter<Item, SellerProfileItemViewHolder> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_profile, container, false);

        sellerLogoutButton = view.findViewById(R.id.seller_logout_button);
        sellerProfile = view.findViewById(R.id.user_image);
        displaySellerName = view.findViewById(R.id.display_seller_name);
        displaySellerCompany = view.findViewById(R.id.display_seller_company);
        displaySellerPhoneNumber = view.findViewById(R.id.display_seller_phone_number);
        sellingItemsRecycleView = view.findViewById(R.id.seller_profile_items_recycle_view);

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(getContext());
        profileStoreage = FirebaseStorage.getInstance().getReference("Seller_Profile_images");
        reference = reference.child(auth.getCurrentUser().getUid());
        sellerItem = sellerItemReference.child(auth.getCurrentUser().getUid());

        sellingItemsRecycleView.setHasFixedSize(true);
        sellingItemsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        options = new FirebaseRecyclerOptions.Builder<Item>().setQuery(sellerItem, Item.class).build();
        adapter = new FirebaseRecyclerAdapter<Item, SellerProfileItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellerProfileItemViewHolder holder, int position, @NonNull Item model) {
                System.out.println(model.getItemId());
                holder.sellerProfileItemName.setText(model.getName());
                holder.sellerProfileItemPrice.setText(model.getPrice());
                holder.sellerProfileItemQuantity.setText(model.getQuantity());
            }

            @NonNull
            @Override
            public SellerProfileItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.single_seller_profile_item, parent, false);
                return new SellerProfileItemViewHolder(v);
            }
        };

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sellerName = snapshot.child("sellerName").getValue().toString();
                sellerEmail = snapshot.child("sellerEmail").getValue().toString();
                shopName = snapshot.child("shopName").getValue().toString();
                sellerPhoneNumber = snapshot.child("phoneNumber").getValue().toString();
                address = snapshot.child("shopAddress").getValue().toString();
                password = snapshot.child("password").getValue().toString();
                type = snapshot.child("type").getValue().toString();

                // after clicking on add product and redirect to it the app was distroyed
                // it says null object referece in line 114
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        sellerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "SellerProfileImage"), GALARY_INTENT);
            }
        });

        loadProfile();
        sellerLogout();
        adapter.startListening();
        sellingItemsRecycleView.setAdapter(adapter);
        return view;
    }

    private void loadProfile() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                displaySellerName.setText(snapshot.child("sellerName").getValue().toString());
                displaySellerCompany.setText(snapshot.child("shopName").getValue().toString());
                displaySellerPhoneNumber.setText(snapshot.child("phoneNumber").getValue().toString());
                if (snapshot.child("profileImage").getValue().toString() != null) {
                    Picasso.get().load(snapshot.child("profileImage").getValue().toString()).into(sellerProfile);
                }
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
                sellerProfile.setImageBitmap(bitmap);

                dialog.setMessage("Adding image...");
                dialog.show();

                final StorageReference imageRef = profileStoreage.child(System.currentTimeMillis() + "." + GetFileExtension(imageUri));
                imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                Seller updateSeller = new Seller(sellerName, sellerEmail, shopName, address, sellerPhoneNumber, password, type, url);
                                reference.setValue(updateSeller);
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

    private void sellerLogout() {
        sellerLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Toast.makeText(getContext(), "You are now sign out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}