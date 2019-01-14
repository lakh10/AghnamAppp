package com.nibrasco.freshksa.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nibrasco.freshksa.Model.Cart;
import com.nibrasco.freshksa.Model.Session;
import com.nibrasco.freshksa.Model.User;
import com.nibrasco.freshksa.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentDetailsFragment extends Fragment {
    private TextView txtCount, txtTotal;
    TextInputEditText edtAccount;
    private Button btnConfirm, btnUpload;
    Cart cart;
    public PaymentDetailsFragment() {
        // Required empty public constructor
        cart = Session.getInstance().Cart();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(com.nibrasco.freshksa.R.layout.fragment_paymentdetails, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        final View v = getView();
        LoadContent(v);
    }

    private void LinkControls(View v){
        txtCount = (TextView)v.findViewById(com.nibrasco.freshksa.R.id.txtOrderCount);
        txtTotal = (TextView)v.findViewById(com.nibrasco.freshksa.R.id.txtOrderTotal);
        edtAccount = (TextInputEditText)v.findViewById(com.nibrasco.freshksa.R.id.edtPaymentAccount);
        btnConfirm = (Button)v.findViewById(com.nibrasco.freshksa.R.id.btnPaymentConfirm);
        btnUpload = (Button)v.findViewById(R.id.btnUpload);
    }
    private void LoadContent(View v){
        LinkControls(v);

        txtTotal.setText(Float.toString(cart.GetTotal()));
        txtCount.setText(Integer.toString(cart.GetCount()));

        LinkListeners(v);
    }
    private void LinkListeners(View v){
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = getResources().getString(com.nibrasco.freshksa.R.string.msgPaymentSaving);
                Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_LONG);
                snackbar.show();
                String Account = edtAccount.getText().toString();

                    cart.setBankAccount(Account);
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    final DatabaseReference tblCart = db.getReference("Cart");
                    final DatabaseReference tblUser = db.getReference("User");
                    final User user = Session.getInstance().User();

                    String cartId = user.getCart(),
                            usrId = user.getPhone();

                    cart.MapToDbRef(tblCart.child(cartId));
                    user.AddOrder(cartId);
                    user.setCart("0");

                    user.MapToDbRef(tblUser.child(usrId));
                    Session.getInstance().User(user);
                    Session.getInstance().Cart(new Cart(cart.getAddress()));

                    snackbar.dismiss();

                    CartFragment cartFragment = new CartFragment();
                    getActivity()
                            .getSupportFragmentManager().beginTransaction()
                            .replace(R.id.homeContainer, cartFragment)
                            .commit();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK)
                                .setType("image/*"),
                        1
                );
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data.getData() != null) {
            StorageReference imgRef = storageRef.child(Session.getInstance().User().getCart());
            imgRef.putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }
    }
}
