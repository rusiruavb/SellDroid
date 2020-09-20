package com.example.selldroid_final;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SellerItems extends Fragment {

    private RecyclerView sellerItemRecyclerView;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference().child("Seller_Items");
    private DatabaseReference sellerReference;
    private UpdateProduct updateProduct;

    private FirebaseRecyclerOptions<Item> options;
    private FirebaseRecyclerAdapter<Item, SellerItemPageViewHolder> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_items, container, false);
        auth = FirebaseAuth.getInstance();
        sellerItemRecyclerView = view.findViewById(R.id.seller_item_recycler_view);
        updateProduct = new UpdateProduct();
        sellerItemRecyclerView.setHasFixedSize(true);
        sellerItemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sellerReference = reference.child(auth.getCurrentUser().getUid());

        options = new FirebaseRecyclerOptions.Builder<Item>().setQuery(sellerReference,Item.class).build();
        adapter = new FirebaseRecyclerAdapter<Item, SellerItemPageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellerItemPageViewHolder holder, int position, @NonNull final Item model) {
                holder.sellerItemName.setText(model.getName());
                holder.sellerItemPrice.setText(model.getPrice());
                holder.sellerItemQuantity.setText(model.getQuantity());
                Picasso.get().load(model.getImageUri()).into(holder.sellerItemImage);

                holder.sellterItemCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("itemId", model.getItemId());
                        bundle.putString("itemName", model.getName());
                        bundle.putString("itemPrice", model.getPrice());
                        bundle.putString("itemQuantity", model.getQuantity());
                        bundle.putString("itemUrl", model.getImageUri());
                        updateProduct.setArguments(bundle);

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_frame, updateProduct);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
            }

            @NonNull
            @Override
            public SellerItemPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_single_item, parent, false);
                return new SellerItemPageViewHolder(v);
            }
        };
        adapter.startListening();
        sellerItemRecyclerView.setAdapter(adapter);
        return view;
    }
}