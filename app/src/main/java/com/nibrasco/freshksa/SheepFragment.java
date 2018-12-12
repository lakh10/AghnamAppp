package com.nibrasco.freshksa;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.nibrasco.freshksa.Model.Cart;
import com.nibrasco.freshksa.Model.Session;

/**
 * A simple {@link Fragment} subclass.
 */
public class SheepFragment extends Fragment {

    Spinner spSlicing, spWeight, spPackaging;
    EditText edtQuantity;
    RadioGroup rdGrpIntestine;
    Boolean IntestineValue;
    public SheepFragment() {
        Session.getInstance().Item().setTotal(Session.getInstance().Item().getDefaultPrice());
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final View v = getView();
        LinkControls(v);
        LoadContent(v);
        LinkListeners();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_naemiorder, container, false);
    }


    void LinkControls(View v)
    {
        spSlicing = (Spinner)v.findViewById(R.id.spSlicing);
        spWeight = (Spinner)v.findViewById(R.id.spWeight);
        spPackaging = (Spinner)v.findViewById(R.id.spPackaging);
        edtQuantity = (EditText)v.findViewById(R.id.edtQuantity);
        rdGrpIntestine = (RadioGroup)v.findViewById(R.id.rdGrpIntestine);
    }
    void LinkListeners()
    {
        rdGrpIntestine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rdInt_Yes:
                        IntestineValue = true;
                        break;
                    case R.id.rdInt_No:
                        IntestineValue = false;
                        break;
                }
            }
        });
        spPackaging.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != spPackaging.getSelectedItemPosition())
                    Session.getInstance().Item().setPackaging(
                        Cart.ePackaging.Get((
                                (Cart.ePackaging)(parent.getItemAtPosition(position)))
                                .Value())
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != spWeight.getSelectedItemPosition())
                    Session.getInstance().Item().setWeight(
                        Cart.eWeight.Get((
                                (Cart.eWeight)(parent.getItemAtPosition(position)))
                                .Value())
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spSlicing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != spSlicing.getSelectedItemPosition()) {
                    Session.getInstance().Item().setSlicing(
                        Cart.eSlicing.Get((
                                (Cart.eSlicing)(parent.getItemAtPosition(position)))
                                .Value())
                );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void LoadContent(View v)
    {
        ArrayAdapter adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, Cart.eSlicing.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSlicing.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(Cart.eSlicing.None);
        spSlicing.setSelection(spinnerPosition);

        adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, Cart.eWeight.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWeight.setAdapter(adapter);
        spinnerPosition = adapter.getPosition(Cart.eWeight.None);
        spWeight.setSelection(spinnerPosition);


        adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, Cart.ePackaging.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPackaging.setAdapter(adapter);
        spinnerPosition = adapter.getPosition(Cart.ePackaging.None);
        spPackaging.setSelection(spinnerPosition);
    }
}
