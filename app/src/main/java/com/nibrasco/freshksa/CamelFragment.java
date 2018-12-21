
package com.nibrasco.freshksa;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.firebase.database.*;
import com.nibrasco.freshksa.Model.Cart;
import com.nibrasco.freshksa.Model.Session;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CamelFragment extends Fragment {

    Spinner spWeight;
    private Button btnConfirm;
    private TextView txtTotal;
    private EditText edtQuantity, edtNotes;
    private Cart.Item currentItem;
    public CamelFragment() {
        currentItem = Session.getInstance().Item();
        currentItem.setQuantity(1);
        currentItem.setWeight(0);
        currentItem.setIntestine(false);
        currentItem.setSlicing(Cart.eSlicing.Fridge.Value());
        currentItem.setPackaging(Cart.ePackaging.None.Value());
        currentItem.setTotal(Session.getInstance().Item().getDefaultPrice());
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_hachiorder, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        final View v = getView();
        LoadContent(v);
    }

    private void LinkControls(View v)
    {
        btnConfirm = (Button)v.findViewById(R.id.btnItemOrder);
        spWeight = (Spinner)v.findViewById(R.id.spWeightCamel);
        edtQuantity = (EditText)v.findViewById(R.id.edtQuantity);
        edtNotes = (EditText)v.findViewById(R.id.edtNotes);
        txtTotal = (TextView)v.findViewById(R.id.txtTotalItem);
    }
    private void LinkListeners()
    {
        spWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != spWeight.getSelectedItemPosition())
                Session.getInstance().Item().setWeight((int)(parent.getItemAtPosition(position)));
                String totalTxt = Float.toString(Session.getInstance().Item().getTotal());
                txtTotal.setText(totalTxt);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int qte = Integer.parseInt(s.toString().equals("") ? "0" : s.toString());
                currentItem.setQuantity(qte);
                txtTotal.setText(Float.toString(currentItem.getTotal()));
            }
        });
        edtNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Session.getInstance().Item().setNotes(s.toString());
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
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.homeContainer, f);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
            }
        });
    }
    private Boolean SaveChanges(View v) {
        final Snackbar snack = Snackbar.make(v, "Saving Your Order", Snackbar.LENGTH_LONG);
        snack.show();
        final Boolean[] success = {true};
        Session.getInstance().Cart().AddItem(Session.getInstance().Item());
        //if(Session.getInstance().User().getCart().equals("0"))
        //{
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference tblCart = db.getReference("Cart");
        tblCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot cartsSnap) {

                DatabaseReference cartRef = tblCart.child(Session.getInstance().User().getCart());
                Session.getInstance().Item().MapToDbRef(cartRef.child("Items"));

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
    private void LoadContent(View v)
    {
        LinkControls(v);

        ArrayList<String> list = Cart.Lists.GetWeightNames(Session.getInstance().Item().getCategory());
        ArrayAdapter adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, list);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWeight.setAdapter(adapter);
        //spWeight.setSelection(0);
        String totalTxt = Float.toString(Session.getInstance().Item().getTotal());
        txtTotal.setText(totalTxt);

        LinkListeners();
    }
}

