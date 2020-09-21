package com.example.selldroid_final;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Map;

public class UpdateProduct extends Fragment {

    private EditText updateItemName;
    private EditText updateItemPrice;
    private EditText updateItemQuantity;
    private ImageView updateItemImage;
    private Button updateProductButton;
    private Button deleteProductButton;
    private Bundle bundle;
    private static final int GALARY_INTENT = 2;
    private Uri imageUri;
    private ProgressDialog mProgressDialog;
    private StorageReference allItemReference;
    private StorageReference storageReference;
    private HomePage home;
    private SellerItems sellerItems;

    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference().child("Seller_Items");
    private DatabaseReference itemReference;
    private DatabaseReference allItems = database.getReference().child("All_Items");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_product, container, false);
        updateItemName = view.findViewById(R.id.update_product_name);
        updateItemPrice = view.findViewById(R.id.update_product_price);
        updateItemQuantity = view.findViewById(R.id.update_product_quantity);
        updateItemImage = view.findViewById(R.id.update_product_image);
        updateProductButton = view.findViewById(R.id.update_product_button);
        deleteProductButton = view.findViewById(R.id.delete_product_button);
        bundle = this.getArguments();
        storageReference = FirebaseStorage.getInstance().getReference("Seller_Items");
        allItemReference = FirebaseStorage.getInstance().getReference("All_Item_Images");
        mProgressDialog = new ProgressDialog(getContext());
        auth = FirebaseAuth.getInstance();
        home = new HomePage();
        sellerItems = new SellerItems();
        itemReference = reference.child(auth.getCurrentUser().getUid());

        displayItemDetails(); // this is display previous item data on the input fields

        updateItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "ProductImage"), GALARY_INTENT);
            }
        });

        if (updateItemImage == null) {

        }

        updateProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemUpdate();
            }
        });

        deleteProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Delete Item");
                alertDialog.setMessage("Do you want to delete this item ?");
                alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        itemDelete();
                    }
                });
                alertDialog.setButton2("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        return view;
    }


    private void itemUpdate() {
        if (imageUri != null) {
            mProgressDialog.setMessage("Updating Item...");
            mProgressDialog.show();

            final StorageReference imageRef4 = allItemReference.child(System.currentTimeMillis() + "." + GetFileExtension(imageUri));
            imageRef4.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final String name = updateItemName.getText().toString().trim();
                    final String productPrice = updateItemPrice.getText().toString().trim();
                    final String productQuantity = updateItemQuantity.getText().toString().trim();
                    final String itemId = bundle.getString("itemId");
                    final String imageUrl = bundle.getString("itemUrl");

                    System.out.println(itemId);
                    imageRef4.delete();

                    mProgressDialog.dismiss();
                    Toast.makeText(getContext(), "Item Updated", Toast.LENGTH_LONG).show();

                    imageRef4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            StorageReference imageRef5 = storageReference.child(auth.getCurrentUser().getUid()).child(System.currentTimeMillis() + "." + GetFileExtension(imageUri));
                            imageRef5.putFile(imageUri); // change the variable name from imageRef4 to imageRef 5
//                            Item newItem = new Item(itemId, name, productPrice, productQuantity, url);
//
//                            allItems.child(bundle.getString("itemId")).setValue(newItem);
//                            reference.child(auth.getCurrentUser().getUid()).child(bundle.getString("itemId")).setValue(newItem);

                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.main_frame, home);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    });
                }
            });
        } else {
            mProgressDialog.dismiss();
            Toast.makeText(getContext(), "Please Select Image", Toast.LENGTH_LONG).show();
        }
    }

    private void itemDelete() {
        mProgressDialog.setMessage("Deleting Item...");
        mProgressDialog.show();

        allItems.child(bundle.getString("itemId")).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                reference.child(auth.getCurrentUser().getUid()).child(bundle.getString("itemId")).removeValue();
                mProgressDialog.dismiss();
                Toast.makeText(getContext(), "Item Deleted", Toast.LENGTH_LONG).show();

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, sellerItems);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void displayItemDetails() {
        updateItemName.setText(bundle.getString("itemName"));
        updateItemPrice.setText(bundle.getString("itemPrice"));
        updateItemQuantity.setText(bundle.getString("itemQuantity"));
        Picasso.get().load(bundle.getString("itemUrl")).into(updateItemImage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALARY_INTENT && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                updateItemImage.setImageBitmap(bitmap);
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
}