package com.nibrasco.freshksa;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.*;
import com.nibrasco.freshksa.Model.Cart;
import com.nibrasco.freshksa.Model.Delivery;
import com.nibrasco.freshksa.Model.Session;
import com.nibrasco.freshksa.Model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentDetailsFragment extends Fragment {
    private TextView txtCount, txtTotal;
    private EditText edtAccount;
    private Button btnConfirm;
    public PaymentDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final View v = getView();
        assert v != null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paymentdetails, container, false);
    }
    private void LinkControls(View v){
        txtCount = (TextView)v.findViewById(R.id.txtOrderCount);
        txtTotal = (TextView)v.findViewById(R.id.txtOrderTotal);
        edtAccount = (EditText)v.findViewById(R.id.edtBankAccount);
        btnConfirm = (Button)v.findViewById(R.id.btnPaymentConfirm);
    }
    private void LoadContent(View v){
        LinkControls(v);

        txtTotal.setText(Float.toString(Session.getInstance().Cart().GetTotal()));
        txtCount.setText(Integer.toString(Session.getInstance().Cart().GetCount()));

        LinkListeners(v);
    }
    private void LinkListeners(View v){
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: insert this into message var below -> getResources().getString(R.string.msgPaymentSaving)
                String message = "Saving Your Order!";
                Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_LONG);
                snackbar.show();
                String Account = edtAccount.getText().toString();

                    final Cart cart = Session.getInstance().Cart();
                    cart.setBankAccount(Account);
                    final User user = Session.getInstance().User();
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    final DatabaseReference tblUser = db.getReference("User"),
                        tblCart = db.getReference("Cart"),
                        tblDelivery = db.getReference("Delivery");
                    tblCart.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot cartSnap) {
                            String cartId = user.getCart();
                            cart.MapToDbRef(tblCart.child(cartId));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    tblDelivery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot deliverySnap) {
                            (new Delivery()).MapToDbRef(tblDelivery);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    tblUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnap) {
                            String cartId = user.getCart(),
                                    usrId = user.getPhone();
                            user.AddOrder(cartId);
                            user.setCart("0");
                            user.MapToDbRef(tblUser.child(usrId));
                            Session.getInstance().User(user);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    snackbar.dismiss();


            }
        });
    }
}
