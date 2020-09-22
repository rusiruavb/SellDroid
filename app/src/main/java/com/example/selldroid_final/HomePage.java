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

public class HomePage extends Fragment {

    private RecyclerView itemRecyclerView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference().child("All_Items");
    private FirebaseRecyclerOptions<Item> options;
    private FirebaseRecyclerAdapter<Item, HomepageViewHolder> adapter;
    private ProductPage productPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        productPage = new ProductPage();
        itemRecyclerView = view.findViewById(R.id.item_recycler_view);
        database = FirebaseDatabase.getInstance();
        itemRecyclerView.setHasFixedSize(true);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        options = new FirebaseRecyclerOptions.Builder<Item>().setQuery(reference, Item.class).build();
        adapter = new FirebaseRecyclerAdapter<Item, HomepageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final HomepageViewHolder holder, int position, @NonNull final Item model) {
                holder.itemName.setText(model.getName());
                holder.itemPrice.setText(model.getPrice());
                holder.itemQuantity.setText(model.getQuantity());
                Picasso.get().load(model.getImageUri()).into(holder.itemImage);

                holder.itemCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("itemImage", model.getImageUri());
                        bundle.putString("itemName", model.getName());
                        bundle.putString("itemPrice", model.getPrice());
                        bundle.putString("itemQuantity", model.getQuantity());
                        bundle.putString("sellerName", model.getSellerName());
                        bundle.putString("sellerEmail", model.getSellerEmail());
                        bundle.putString("sellerPhoneNumber", model.getSellerPhoneNumber());
                        bundle.putString("shopName", model.getShopName());
                        bundle.putString("shopAddress", model.getShopAddress());
                        bundle.putString("type", model.getType());
                        bundle.putString("description", model.getDescription());
                        productPage.setArguments(bundle);

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_frame, productPage);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
            }

            @NonNull
            @Override
            public HomepageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item, parent, false);
                return new HomepageViewHolder(v);
            }
        };
        adapter.startListening();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        itemRecyclerView.setLayoutManager(gridLayoutManager);
        itemRecyclerView.setAdapter(adapter);
        return view;
    }
}