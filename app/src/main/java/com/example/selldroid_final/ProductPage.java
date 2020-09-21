package com.example.selldroid_final;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ProductPage extends Fragment {

    private ImageView productImage;
    private TextView productName;
    private TextView productPrice;
    private TextView productQuantity;
    private TextView sellerName;
    private TextView sellerEmail;
    private TextView sellerStoreName;
    private TextView sellerPhoneNumber;
    private TextView sellerAddress;
    private Button addToCartButton;
    private Button buyNowButton;
    private Bundle bundle;

    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference allItems = database.getReference().child("All_Items");
    private DatabaseReference cartReference = database.getReference().child("Cart");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_page, container, false);
        bundle = this.getArguments();
        productImage = view.findViewById(R.id.product_display_image);
        productName = view.findViewById(R.id.product_display_name);
        productPrice = view.findViewById(R.id.product_display_price);
        productQuantity = view.findViewById(R.id.product_display_quantity);
        sellerName = view.findViewById(R.id.product_display_seller_name);
        sellerEmail = view.findViewById(R.id.product_display_seller_email);
        sellerStoreName = view.findViewById(R.id.product_display_seller_store_name);
        sellerPhoneNumber = view.findViewById(R.id.product_display_seller_phone_number);
        sellerAddress = view.findViewById(R.id.product_display_seller_address);
        addToCartButton = view.findViewById(R.id.product_display_add_to_cart_button);
        buyNowButton = view.findViewById(R.id.product_display_buy_now_button);

        auth = FirebaseAuth.getInstance();
        cartReference = cartReference.child(auth.getCurrentUser().getUid());

        displayItemDetails();

        addToCart();

        return view;
    }

    private void addToCart() {
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = bundle.getString("itemName");
                String itemPrice = bundle.getString("itemPrice");
                String itemImage = bundle.getString("itemImage");
                String itemId = cartReference.push().getKey();
                int itemQuantity = 1;
                int totalPrice = Integer.parseInt(itemPrice);

                Cart newCartItem = new Cart(itemId, itemName, itemPrice, itemImage, itemQuantity, totalPrice);
                cartReference.child(itemId).setValue(newCartItem);
                Toast.makeText(getContext(), "Item Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayItemDetails() {
        Picasso.get().load(bundle.getString("itemImage")).into(productImage);
        productName.setText(bundle.getString("itemName"));
        productPrice.setText(bundle.getString("itemPrice"));
        productQuantity.setText(bundle.getString("itemQuantity"));
        sellerName.setText(bundle.getString("sellerName"));
        sellerEmail.setText(bundle.getString("sellerEmail"));
        sellerStoreName.setText(bundle.getString("shopName"));
        sellerPhoneNumber.setText(bundle.getString("sellerPhoneNumber"));
        sellerAddress.setText(bundle.getString("shopAddress"));
    }
}