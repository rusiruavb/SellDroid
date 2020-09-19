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

    private EditText productName;
    private EditText price;
    private EditText quantity;
    private Button addButton;
    private ImageView productImg;
    private ProgressDialog mProgressDialog;
    private String userId;
    private static final int GALARY_INTENT = 2;
    private Uri imageUri;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference = mFirebaseDatabase.getReference().child("Seller_Items");
    private DatabaseReference allItems = mFirebaseDatabase.getReference().child("All_Items");
    private StorageReference storageReference;
    private StorageReference allItemReference;

    private HomePage home;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        productName = view.findViewById(R.id.add_product_name);
        price = view.findViewById(R.id.add_product_price);
        quantity = view.findViewById(R.id.add_product_quantity);
        addButton = view.findViewById(R.id.add_product_button);
        productImg = view.findViewById(R.id.add_product_image);
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Seller_Items");
        allItemReference = FirebaseStorage.getInstance().getReference("All_Item_Images");
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
                    final String imgUri = allItemReference.getDownloadUrl().toString();

                    mProgressDialog.dismiss();
                    Toast.makeText(getContext(), "Item Added", Toast.LENGTH_LONG).show();

                    imageRef4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            StorageReference imageRef4 = storageReference.child(mAuth.getCurrentUser().getUid()).child(System.currentTimeMillis() + "." + GetFileExtension(imageUri));
                            imageRef4.putFile(imageUri);
                            Item newItem = new Item(name, productPrice, productQuantity, url);
                            String imageUploadId = mReference.push().getKey();

                            allItems.child(imageUploadId).setValue(newItem);
                            mReference.child(mAuth.getCurrentUser().getUid()).child(imageUploadId).setValue(newItem);
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.main_frame, home);
                            transaction.commit();
                        }
                    });

//                    StorageReference imageRef4 = storageReference.child(mAuth.getCurrentUser().getUid()).child(System.currentTimeMillis() + "." + GetFileExtension(imageUri));
//                    imageRef4.putFile(imageUri);
//                    Item newItem = new Item(name, productPrice, productQuantity, imgUri);
//                    String imageUploadId = mReference.push().getKey();
//
//                    allItems.child(imageUploadId).setValue(newItem);
//                    mReference.child(mAuth.getCurrentUser().getUid()).child(imageUploadId).setValue(newItem);
//                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.main_frame, home);
//                    transaction.commit();
                }
            });

//            final StorageReference imageRef2 = storageReference.child(mAuth.getCurrentUser().getUid()).child(System.currentTimeMillis() + "." + GetFileExtension(imageUri));
//            imageRef2.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    String name = productName.getText().toString().trim();
//                    String productPrice = price.getText().toString().trim();
//                    String productQuantity = quantity.getText().toString().trim();
//                    //String image = taskSnapshot.getUploadSessionUri().toString();
//                    String imgUri = imageRef2.getDownloadUrl().toString();
//
//                    mProgressDialog.dismiss();
//                    Toast.makeText(getContext(), "Item Added", Toast.LENGTH_LONG).show();
//                    StorageReference imageRef3 = allItemReference.child(System.currentTimeMillis() + "." + GetFileExtension(imageUri));
//                    imageRef3.putFile(imageUri);
//                    Item newItem = new Item(name, productPrice, productQuantity, imgUri);
//                    String imageUploadId = mReference.push().getKey();
//                    mReference.child(mAuth.getCurrentUser().getUid()).child(imageUploadId).setValue(newItem);
//                    allItems.child(imageUploadId).setValue(newItem);
//                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.main_frame, home);
//                    transaction.commit();
//                }
//            });
        } else {
            Toast.makeText(getContext(), "Please Select An Image", Toast.LENGTH_LONG).show();
        }
    }
}