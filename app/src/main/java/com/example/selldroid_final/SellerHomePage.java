package com.example.selldroid_final;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SellerHomePage extends Fragment {

    private RecyclerView sellerHomeRecyclerView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference().child("All_Items");
    private FirebaseRecyclerOptions<Item> options;
    private FirebaseRecyclerAdapter<Item, SellerHomeItemViewHolder> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_home_page, container, false);

        sellerHomeRecyclerView = view.findViewById(R.id.seller_home_recycler_view);
        sellerHomeRecyclerView.setHasFixedSize(true);
        sellerHomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        options = new FirebaseRecyclerOptions.Builder<Item>().setQuery(reference, Item.class).build();

        adapter = new FirebaseRecyclerAdapter<Item, SellerHomeItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final SellerHomeItemViewHolder holder, int position, @NonNull final Item model) {
                holder.itemName.setText(model.getName());
                holder.itemPrice.setText(model.getPrice());
                holder.itemQuantity.setText(model.getQuantity());
                Picasso.get().load(model.getImageUri()).centerCrop().fit().into(holder.itemImage);
            }

            @NonNull
            @Override
            public SellerHomeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.single_seller_homepage_item, parent, false);
                return new SellerHomeItemViewHolder(v);
            }
        };

        adapter.startListening();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        sellerHomeRecyclerView.setLayoutManager(gridLayoutManager);
        sellerHomeRecyclerView.setAdapter(adapter);

        return view;
    }
}