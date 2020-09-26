package com.example.selldroid_final;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CartItems extends Fragment {

    private RecyclerView cartRecycleView;
    private TextView cartTotalPrice;
    private Button buyNowButton;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference().child("Cart");
    private FirebaseRecyclerOptions<Cart> options;
    private FirebaseRecyclerAdapter<Cart, CartItemViewHolder> adapter;
    private int sum = 0;
    private CartItems cartItems;
    private int databaseItemQuantity;
    private int databaseItemQuantityForRemove;
    private int totalSum;
    private int value;
    private BuyNow buyNow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        auth = FirebaseAuth.getInstance();
        cartItems = new CartItems();
        buyNow = new BuyNow();
        cartTotalPrice = view.findViewById(R.id.cart_item_price);
        cartRecycleView = view.findViewById(R.id.cart_recycle_view);
        buyNowButton = view.findViewById(R.id.buy_now_button);
        cartRecycleView.setHasFixedSize(true);
        cartRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(reference.child(auth.getCurrentUser().getUid()), Cart.class).build();
        adapter = new FirebaseRecyclerAdapter<Cart, CartItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CartItemViewHolder holder, int position, @NonNull final Cart model) {
                holder.cartItemName.setText(model.getItemName());
                holder.cartItemPrice.setText(model.getItemPrice());
                holder.cartItemQuantity.setText(String.valueOf(model.getItemQuantity()));
                Picasso.get().load(model.getItemImage()).into(holder.cartItemImage);
                // cartTotalPrice.setText(String.valueOf(sum));

                holder.cartItemDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reference.child(auth.getCurrentUser().getUid()).child(model.getCartId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                System.out.println("Item Deleted");
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.main_frame, cartItems);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        });
                    }
                });

                holder.addItemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        databaseItemQuantity = model.getItemQuantity();
                        ++databaseItemQuantity;
                        totalSum = Integer.parseInt(model.getItemPrice()) * databaseItemQuantity;
                        cartTotalPrice.setText(String.valueOf(totalSum));
                        holder.cartItemQuantity.setText(String.valueOf(databaseItemQuantity));
                        reference.child(auth.getCurrentUser().getUid()).child(model.getCartId()).child("itemQuantity").setValue(databaseItemQuantity);
                        reference.child(auth.getCurrentUser().getUid()).child(model.getCartId()).child("totalPrice").setValue(totalSum);
                    }
                });

                holder.removeItemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        databaseItemQuantityForRemove = model.getItemQuantity();
                        --databaseItemQuantityForRemove;
                        totalSum = Integer.parseInt(model.getItemPrice()) * databaseItemQuantityForRemove;
                        cartTotalPrice.setText(String.valueOf(totalSum));
                        holder.cartItemQuantity.setText(String.valueOf(databaseItemQuantityForRemove));
                        reference.child(auth.getCurrentUser().getUid()).child(model.getCartId()).child("itemQuantity").setValue(databaseItemQuantityForRemove);
                        reference.child(auth.getCurrentUser().getUid()).child(model.getCartId()).child("totalPrice").setValue(totalSum);
                    }
                });
            }

            @NonNull
            @Override
            public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cart_item, parent, false);
                return new CartItemViewHolder(v);
            }
        };

        buyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String dateTime = dateFormat.format(date);
                Bundle bundle = new Bundle();
                bundle.putString("totalPrice", String.valueOf(sum));
                bundle.putString("date", dateTime);
                buyNow.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, buyNow);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        calculateTotal();
        adapter.startListening();
        cartRecycleView.setAdapter(adapter);
        return view;
    }

    private void calculateTotal() {
        reference.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object totalPrice = map.get("totalPrice");
                    value = Integer.parseInt(String.valueOf(totalPrice));
                    sum += value;
                    cartTotalPrice.setText(String.valueOf(sum));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}