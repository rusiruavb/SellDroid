package com.example.selldroid_final;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class AddProduct extends Fragment {

    private EditText description;
    private EditText productName;
    private EditText price;
    private EditText quantity;
    private Button addButton;
    private ImageView productImg;
    private ProgressDialog mProgressDialog;
    private String userId;
    private static final int GALARY_INTENT = 2;
    private Uri imageUri;

    private String sellerName;
    private String sellerEmail;
    private String sellerShopName;
    private String sellerPhoneNumber;
    private String sellerAddress;
    private String type;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference = mFirebaseDatabase.getReference().child("Seller_Items");
    private DatabaseReference allItems = mFirebaseDatabase.getReference().child("All_Items");
    private DatabaseReference sellerDetails = mFirebaseDatabase.getReference().child("Seller");
    private DatabaseReference getDetails;
    private StorageReference storageReference;
    private StorageReference allItemReference;

    private HomePage home;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        description = view.findViewById(R.id.add_product_description);
        productName = view.findViewById(R.id.add_product_name);
        price = view.findViewById(R.id.add_product_price);
        quantity = view.findViewById(R.id.add_product_quantity);
        addButton = view.findViewById(R.id.add_product_button);
        productImg = view.findViewById(R.id.add_product_image);
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Seller_Items");
        allItemReference = FirebaseStorage.getInstance().getReference("All_Item_Images");

        sellerDetails.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sellerName = snapshot.child("sellerName").getValue().toString();
                sellerEmail = snapshot.child("sellerEmail").getValue().toString();
                sellerShopName = snapshot.child("shopName").getValue().toString();
                sellerPhoneNumber = snapshot.child("phoneNumber").getValue().toString();
                sellerAddress = snapshot.child("shopAddress").getValue().toString();
                type = snapshot.child("type").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mProgressDialog = new ProgressDialog(getActivity());
        home = new HomePage();

        productImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "ProductImage"), GALARY_INTENT);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendItem();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALARY_INTENT && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                productImg.setImageBitmap(bitmap);
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

    private void sendItem() {
        if (imageUri != null) {
            mProgressDialog.setMessage("Adding Item...");
            mProgressDialog.show();

            final StorageReference imageRef4 = allItemReference.child(System.currentTimeMillis() + "." + GetFileExtension(imageUri));
            imageRef4.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final String name = productName.getText().toString().trim();
                    final String productPrice = price.getText().toString().trim();
                    final String productQuantity = quantity.getText().toString().trim();
                    final String desc = description.getText().toString().trim();
                    final String itemId = mReference.push().getKey();

                    mProgressDialog.dismiss();
                    Toast.makeText(getContext(), "Item Added", Toast.LENGTH_LONG).show();

                    imageRef4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            StorageReference imageRef4 = storageReference.child(mAuth.getCurrentUser().getUid()).child(System.currentTimeMillis() + "." + GetFileExtension(imageUri));
                            imageRef4.putFile(imageUri);
                            Item newItem = new Item(itemId, name, productPrice, productQuantity, url, sellerName, sellerEmail, sellerPhoneNumber, sellerShopName, sellerAddress, type, desc);
                            allItems.child(itemId).setValue(newItem);
                            mReference.child(mAuth.getCurrentUser().getUid()).child(itemId).setValue(newItem);

                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.seller_main_frame, new SellerHomePage());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    });
                }
            });

        } else {
            Toast.makeText(getContext(), "Please Select An Image", Toast.LENGTH_LONG).show();
        }
    }
}