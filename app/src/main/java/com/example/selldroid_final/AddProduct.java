package com.example.selldroid_final;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class AddProduct extends Fragment {

    private EditText productName;
    private EditText price;
    private EditText quantity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        productName = view.findViewById(R.id.add_product_name);
        price = view.findViewById(R.id.add_product_price);
        quantity = view.findViewById(R.id.add_product_quantity);
        return view;
    }
}