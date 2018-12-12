
package com.nibrasco.freshksa;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.nibrasco.freshksa.Model.Cart;
import com.nibrasco.freshksa.Model.Session;

/**
 * A simple {@link Fragment} subclass.
 */
public class CamelFragment extends Fragment {

    Spinner spWeight;
    EditText edtQuantity;
    public CamelFragment() {
        // Required empty public constructor
        Session.getInstance().Item().setIntestine(false);
        Session.getInstance().Item().setSlicing(Cart.eSlicing.None);
        Session.getInstance().Item().setPackaging(Cart.ePackaging.None);
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
        return inflater.inflate(R.layout.fragment_hachiorder, container, false);
    }


    void LinkControls(View v)
    {
        spWeight = (Spinner)v.findViewById(R.id.spWeight);
        edtQuantity = (EditText)v.findViewById(R.id.edtQuantity);
    }
    void LinkListeners()
    {
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
    }
    private void LoadContent(View v)
    {
        ArrayAdapter adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, Cart.eWeight.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWeight.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(Cart.eWeight.None);
        spWeight.setSelection(spinnerPosition);
    }
}

