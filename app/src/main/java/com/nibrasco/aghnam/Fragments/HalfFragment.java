package com.nibrasco.aghnam.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.*;
import com.nibrasco.aghnam.Model.Cart;
import com.nibrasco.aghnam.Model.Session;
import com.nibrasco.aghnam.R;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

/**
 * A simple {@link Fragment} subclass.
 */
public class HalfFragment extends Fragment {

    Button btnConfirm;
    NumberPicker edtQuantity;
    Cart.Item currentItem;
    private TextView txtTotal;
    public HalfFragment() {
        // Required empty public constructor
        currentItem = Session.getInstance().Item();
        currentItem.setQuantity(1);
        currentItem.setIntestine(false);
        currentItem.setWeight(0);
        currentItem.setPackaging(Cart.ePackaging.Bags.Value());
        currentItem.setSlicing(Cart.eSlicing.Fridge.Value());
        currentItem.setNotes("");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(com.nibrasco.aghnam.R.layout.fragment_nesfnaemiorder, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        final View v = getView();
        LoadContent(v);
    }
    private void LoadContent(View v){
        btnConfirm = (Button)v.findViewById(com.nibrasco.aghnam.R.id.btnItemOrder);
        edtQuantity = (NumberPicker)v.findViewById(com.nibrasco.aghnam.R.id.edtQuantity);
        txtTotal = (TextView)v.findViewById(com.nibrasco.aghnam.R.id.txtTotalItem);

        txtTotal.setText(Float.toString(currentItem.getTotal()));
        edtQuantity.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                int qte = value;
                currentItem.setQuantity(qte);
                txtTotal.setText(Float.toString(currentItem.getTotal()));
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Session.getInstance().Item().getCategory() != Cart.eCategory.None) {
                    //Add the item to the cart at this point
                    if(SaveChanges(v))
                    {
                        CartFragment f = new CartFragment();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.homeContainer, f)
                                .commit();
                    }
                }
            }
        });

    }
    private Boolean SaveChanges(View v) {
        try {
            final Snackbar snack = Snackbar.make(v, "Saving Your Order", Snackbar.LENGTH_LONG);
            snack.show();
            final Boolean[] success = {true};
            currentItem.setId(Session.getInstance().Cart().GetCount());
            Session.getInstance().Cart().AddItem(currentItem);
            //if(Session.getInstance().User().getCart().equals("0"))
            //{
            final FirebaseDatabase db = FirebaseDatabase.getInstance();
            final DatabaseReference tblCart = db.getReference("Cart");
            tblCart.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot cartsSnap) {

                    DatabaseReference cartRef = tblCart.child(Session.getInstance().User().getCart());
                    currentItem.MapToDbRef(cartRef.child("Items").child(Integer.toString(currentItem.getId())));
                    snack.dismiss();
                    success[0] = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    success[0] = false;
                }
            });
            //}
            return success[0];
        }
        catch (Exception e){
            Log.e(HalfFragment.class.getName(), e.getMessage());
            return false;
        }
    }
}
