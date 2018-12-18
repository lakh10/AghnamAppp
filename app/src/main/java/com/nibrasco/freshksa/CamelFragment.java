
package com.nibrasco.freshksa;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.nibrasco.freshksa.Model.Cart;
import com.nibrasco.freshksa.Model.Session;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CamelFragment extends Fragment {

    Spinner spWeight;
    private TextView txtTotal;
    private EditText edtQuantity, edtNotes;
    public CamelFragment() {
        Session.getInstance().Item().setWeight(0);
        Session.getInstance().Item().setIntestine(false);
        Session.getInstance().Item().setSlicing(Cart.eSlicing.Fridge.Value());
        Session.getInstance().Item().setPackaging(Cart.ePackaging.None.Value());
        Session.getInstance().Item().setTotal(Session.getInstance().Item().getDefaultPrice());
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
                Session.getInstance().Item().setQuantity(Integer.parseInt(s.toString()));
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
    }
    private void LoadContent(View v)
    {
        LinkControls(v);

        ArrayList<String> list = Cart.Lists.GetWeightNames(Session.getInstance().Item().getCategory());
        ArrayAdapter adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, list);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWeight.setAdapter(adapter);
        spWeight.setSelection(0);
        String totalTxt = Float.toString(Session.getInstance().Item().getTotal());
        txtTotal.setText(totalTxt);

        LinkListeners();
    }
}

