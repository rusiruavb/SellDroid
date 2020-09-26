package com.example.selldroid_final;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BuyNow extends Fragment {

    private RecyclerView paymentMethodRecyclerView;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference().child("Payment_Methods");
    private DatabaseReference paymentMethodReference;
    private DatabaseReference conformPaymentReference = database.getReference().child("User_Payments");
    private DatabaseReference cartReference = database.getReference().child("Cart");
    private FirebaseRecyclerOptions<PaymentMethod> options;
    private FirebaseRecyclerAdapter<PaymentMethod, BuyNowViewHolder> adapter;
    private Bundle bundle;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_now, container, false);

        auth = FirebaseAuth.getInstance();
        bundle = getArguments();
        dialog = new ProgressDialog(getContext());
        paymentMethodRecyclerView = view.findViewById(R.id.payment_method_recycle_view);
        paymentMethodRecyclerView.setHasFixedSize(true);
        paymentMethodRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        paymentMethodReference = reference.child(auth.getCurrentUser().getUid());

        options = new FirebaseRecyclerOptions.Builder<PaymentMethod>().setQuery(paymentMethodReference, PaymentMethod.class).build();
        adapter = new FirebaseRecyclerAdapter<PaymentMethod, BuyNowViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BuyNowViewHolder holder, int position, @NonNull final PaymentMethod model) {
                holder.cardNumber.setText(model.getCardNumber());
                holder.cartHolderName.setText(model.getHolderName());
                holder.cardPhoneNumber.setText(model.getPhoneNumber());
                holder.cardValidation.setText(model.getValidationPeriod());

                holder.paymentMethod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setCancelable(false);
                        alertDialog.setTitle("Conform Your Payment");
                        alertDialog.setMessage("Are you sure ?");
                        alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String paymentId = model.getPaymentMethodId();
                                String dateTime = bundle.getString("date");
                                String totalPrice = bundle.getString("totalPrice");
                                String pushId = conformPaymentReference.push().getKey();

                                ConformedPayment newConformedPayment = new ConformedPayment(paymentId, dateTime, totalPrice);
                                conformPaymentReference.child(auth.getCurrentUser().getUid()).child(pushId).setValue(newConformedPayment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            cartReference.child(auth.getCurrentUser().getUid()).removeValue();
                                            Toast.makeText(getContext(), "Payment Success", Toast.LENGTH_SHORT).show();
                                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                            transaction.replace(R.id.main_frame, new UserProfile());
                                            transaction.addToBackStack(null);
                                            transaction.commit();
                                        } else {
                                            Toast.makeText(getContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
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
            }

            @NonNull
            @Override
            public BuyNowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.single_payment_method, parent, false);
                return new BuyNowViewHolder(v);
            }
        };



        adapter.startListening();
        paymentMethodRecyclerView.setAdapter(adapter);
        return view;
    }
}