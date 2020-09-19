package com.example.selldroid_final;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

public class SellerItems extends Fragment {

    private RecyclerView sellerItemRecyclerView;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference().child("Seller_Items");

    private FirebaseRecyclerOptions<Item> options;
    private FirebaseRecyclerAdapter<Item, SellerItemPageViewHolder> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_items, container, false);
        auth = FirebaseAuth.getInstance();
        sellerItemRecyclerView = view.findViewById(R.id.seller_item_recycler_view);
        sellerItemRecyclerView.setHasFixedSize(true);
        sellerItemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        options = new FirebaseRecyclerOptions.Builder<Item>().setQuery(reference,Item.class).build();
        // last day work is done up to here
        // need to create SellerItemPageViewHolder class
        return view;
    }
}